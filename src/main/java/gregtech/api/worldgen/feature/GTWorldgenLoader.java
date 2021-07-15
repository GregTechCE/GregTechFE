package gregtech.api.worldgen.feature;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import gregtech.api.GTValues;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

//TODO this is horrible, we really need to find a better way to approach dynamic ore vein loading
@SuppressWarnings("deprecation")
public class GTWorldgenLoader extends SinglePreparationResourceReloader<Map<String, List<Pair<Identifier, ConfiguredFeature<?, ?>>>>> implements IdentifiableResourceReloadListener {

    private static final Identifier LISTENER_ID = new Identifier(GTValues.MODID, "worldgen_loader");
    private static final Logger LOGGER = LoggerFactory.getLogger(GTWorldgenLoader.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    private static final String GT_WORLDGEN_FOLDER = "gt_worldgen";
    private static final String WORLDGEN_FILE_EXTENSION = ".json";
    private static final String CATEGORY_PREFIX = "category_";

    public static final GTWorldgenLoader INSTANCE = new GTWorldgenLoader();

    private Map<Predicate<BiomeSelectionContext>, List<RegistryKey<ConfiguredFeature<?, ?>>>> registeredFeatures = ImmutableMap.of();

    private GTWorldgenLoader() {
    }

    public void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(this);
        BiomeModifications.create(LISTENER_ID).add(ModificationPhase.ADDITIONS, context -> true, this::applyFeatures);
    }

    private void applyFeatures(BiomeSelectionContext selectionContext, BiomeModificationContext modificationContext) {
        BiomeModificationContext.GenerationSettingsContext generationSettingsContext = modificationContext.getGenerationSettings();

        for (var entry : this.registeredFeatures.entrySet()) {
            if (entry.getKey().test(selectionContext)) {
                entry.getValue().forEach(featureKey -> generationSettingsContext.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, featureKey));
            }
        }
    }

    private static Predicate<BiomeSelectionContext> createBiomeSelectorFromString(String selectorString) {
        if (selectorString.startsWith(CATEGORY_PREFIX)) {
            String categoryName = selectorString.substring(CATEGORY_PREFIX.length());
            Biome.Category category = Biome.Category.byName(categoryName);

            if (category == null) {
                throw new RuntimeException("Category not found by the name: " + categoryName);
            }
            return BiomeSelectors.categories(category);
        }

        return switch (selectorString) {
            case "overworld" -> BiomeSelectors.foundInOverworld();
            case "the_nether" -> BiomeSelectors.foundInTheNether();
            case "the_end" -> BiomeSelectors.foundInTheEnd();
            default -> throw new RuntimeException("Dimension not found by name: " + selectorString);
        };
    }

    private static ConfiguredFeature<?, ?> parseWorldgenDefinition(DynamicOps<JsonElement> ops, JsonElement jsonElement) {
        DataResult<ConfiguredFeature<?, ?>> dataResult = ConfiguredFeature.CODEC.parse(ops, jsonElement);
        if (dataResult.error().isPresent()) {
            throw new RuntimeException("Failed to decode worldgen definition: " + dataResult.error().get().message());
        }
        return dataResult.result().orElseThrow();
    }

    @Override
    protected Map<String, List<Pair<Identifier, ConfiguredFeature<?, ?>>>> prepare(ResourceManager manager, Profiler profiler) {
        DynamicRegistryManager dynamicRegistryManager = DynamicRegistryManager.create();
        RegistryReadingOps<JsonElement> registryReadingOps = RegistryReadingOps.of(JsonOps.INSTANCE, dynamicRegistryManager);

        Collection<Identifier> resourcePaths = manager.findResources(GT_WORLDGEN_FOLDER, path -> path.endsWith(WORLDGEN_FILE_EXTENSION));
        int rootPathOffset = GT_WORLDGEN_FOLDER.length() + 1;

        HashMap<String, List<Pair<Identifier, ConfiguredFeature<?, ?>>>> map = new HashMap<>();

        for (Identifier resourcePath : resourcePaths) {
            String resourcePathString = resourcePath.getPath();
            int nextSlashIndex = resourcePathString.indexOf('/', rootPathOffset);

            //Skip worldgen definitions not assigned to any particular biome or dimension type
            if (nextSlashIndex == -1) {
                LOGGER.error("Found unassigned worldgen definition {}, it will be skipped", resourcePath);
                continue;
            }

            int lastPathIndex = resourcePathString.length() - WORLDGEN_FILE_EXTENSION.length();

            String biomeFilterString = resourcePathString.substring(rootPathOffset, nextSlashIndex);
            String oreVeinPath = resourcePathString.substring(nextSlashIndex + 1, lastPathIndex);

            Identifier oreVeinId = new Identifier(resourcePath.getNamespace(), oreVeinPath);
            List<Pair<Identifier, ConfiguredFeature<?, ?>>> list = map.computeIfAbsent(biomeFilterString, k -> new ArrayList<>());

            try {
                try(Resource resource = manager.getResource(resourcePath)) {
                    InputStream inputStream = resource.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                    JsonElement jsonElement = JsonHelper.deserialize(GSON, reader, JsonElement.class);
                    ConfiguredFeature<?, ?> config = parseWorldgenDefinition(registryReadingOps, jsonElement);
                    list.add(Pair.of(oreVeinId, config));
                }
            } catch (Exception exception) {
                LOGGER.error("Failed to load worldgen definition {}", resourcePath, exception);
            }
        }
        return map;
    }

    @Override
    protected void apply(Map<String, List<Pair<Identifier, ConfiguredFeature<?, ?>>>> prepared, ResourceManager manager, Profiler profiler) {
        Map<Predicate<BiomeSelectionContext>, List<RegistryKey<ConfiguredFeature<?, ?>>>> resultMap = new HashMap<>();
        MutableRegistry<ConfiguredFeature<?, ?>> registry = (MutableRegistry<ConfiguredFeature<?,?>>) BuiltinRegistries.CONFIGURED_FEATURE;
        
        try {
            for (var entry : prepared.entrySet()) {
                Predicate<BiomeSelectionContext> biomeSelectionContextPredicate = createBiomeSelectorFromString(entry.getKey());
                ArrayList<RegistryKey<ConfiguredFeature<?, ?>>> resultList = new ArrayList<>();
                
                for (Pair<Identifier, ConfiguredFeature<?, ?>> pair : entry.getValue()) {
                    RegistryKey<ConfiguredFeature<?, ?>> registryKey = RegistryKey.of(registry.getKey(), pair.getLeft());
                    registry.replace(OptionalInt.empty(), registryKey, pair.getRight(), Lifecycle.experimental());
                    resultList.add(registryKey);
                }
                resultMap.put(biomeSelectionContextPredicate, resultList);
            }
            
            this.registeredFeatures = ImmutableMap.copyOf(resultMap);
        } catch (Exception exception) {
            LOGGER.error("Failed to reload worldgen definitions", exception);
        }
    }

    @Override
    public Identifier getFabricId() {
        return LISTENER_ID;
    }
}
