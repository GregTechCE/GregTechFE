package gregtech.api.recipe;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import gregtech.api.network.PacketMachineRecipesSynchronize;
import gregtech.mixin.impl.GTClientPlayNetworkHandler;
import gregtech.mixin.impl.GTServerResourceManager;
import gregtech.mixin.accessor.ClientWorldAccessor;
import gregtech.mixin.accessor.MinecraftServerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GTRecipeManager extends SinglePreparationResourceReloader<Map<String, List<MachineRecipe<?, ?>>>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GTRecipeManager.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final String GT_MACHINE_RECIPES_FOLDER = "gt_machine_recipes";
    private static final String RECIPE_EXTENSION = ".json";

    private final RecipeManager recipeManager;
    private final Map<MachineRecipeType, RecipeMap> recipes = new HashMap<>();

    public GTRecipeManager(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    @Nullable
    public static GTRecipeManager get(World world) {
        if (world instanceof ServerWorld serverWorld) {
            MinecraftServer server = serverWorld.getServer();
            ServerResourceManager resourceManager = ((MinecraftServerAccessor) server).getServerResourceManager();

            return ((GTServerResourceManager) resourceManager).gregtech_getRecipeManager();
        }
        if (world instanceof ClientWorld clientWorld) {
            ClientPlayNetworkHandler netHandler = ((ClientWorldAccessor) clientWorld).getNetHandler();

            return ((GTClientPlayNetworkHandler) netHandler).gregtech_getRecipeManager();
        }
        return null;
    }

    @Environment(EnvType.CLIENT)
    public static void handleRecipeSync(MinecraftClient client, ClientPlayNetworkHandler handler, PacketMachineRecipesSynchronize packet) {
        client.submit(() -> {
            GTRecipeManager recipeManager = ((GTClientPlayNetworkHandler) handler).gregtech_getRecipeManager();
            recipeManager.setRecipes(packet.getRecipes());
        });
    }

    @Nullable
    public RecipeMap getRecipesOfType(MachineRecipeType recipeType) {
        Preconditions.checkNotNull(recipeType, "recipeType");
        return this.recipes.get(recipeType);
    }

    private static MachineRecipe<?, ?> parseRecipe(Identifier recipeId, JsonElement jsonElement) {
        if (jsonElement == null) {
            throw new JsonParseException("Expected file root to be an object, got null or empty file");
        }
        JsonObject object = JsonHelper.asObject(jsonElement, "file root");
        String recipeType = JsonHelper.getString(object, "type");
        Identifier recipeTypeId = Identifier.tryParse(recipeType);

        if (recipeTypeId == null) {
            throw new JsonParseException("Couldn't parse recipe type, expected identifier");
        }

        RecipeSerializer<?> recipeSerializer = RecipeSerializer.REGISTRY.get(recipeTypeId);
        if (recipeSerializer == null) {
            throw new JsonParseException("Recipe serializer not found for type " + recipeTypeId);
        }
        return recipeSerializer.read(recipeId, object);
    }

    private static Map<String, MachineRecipeType> buildRecipeTypeIndex()  {
        HashMap<String, MachineRecipeType> recipeTypesByFullName = new HashMap<>();

        for (Identifier identifier : MachineRecipeType.REGISTRY.getIds()) {
            String normalizedPath = identifier.getPath().replace('/', '_');
            String fullPath = identifier.getNamespace() + '_' + normalizedPath;
            MachineRecipeType recipeType = MachineRecipeType.REGISTRY.get(identifier);

            recipeTypesByFullName.put(fullPath, recipeType);
        }
        return recipeTypesByFullName;
    }

    @Override
    protected Map<String, List<MachineRecipe<?, ?>>> prepare(ResourceManager manager, Profiler profiler) {
        HashMap<String, List<MachineRecipe<?, ?>>> map = new HashMap<>();

        Collection<Identifier> resourcePaths = manager.findResources(GT_MACHINE_RECIPES_FOLDER,
                path -> path.endsWith(RECIPE_EXTENSION));

        int rootPathOffset = GT_MACHINE_RECIPES_FOLDER.length() + 1;
        for (Identifier resourcePath : resourcePaths) {
            String resourcePathString = resourcePath.getPath();
            int nextSlashIndex = resourcePathString.indexOf('/', rootPathOffset);

            //Skip uncategorized machine recipes and print warnings
            if (nextSlashIndex == -1) {
                LOGGER.error("Found uncategorized machine recipe at {}, it will be skipped", resourcePath);
                continue;
            }

            int lastPathIndex = resourcePathString.length() - RECIPE_EXTENSION.length();

            String recipeMapId = resourcePathString.substring(rootPathOffset, nextSlashIndex);
            String recipePath = resourcePathString.substring(nextSlashIndex + 1, lastPathIndex);

            Identifier recipeId = new Identifier(resourcePath.getNamespace(), recipePath);
            List<MachineRecipe<?, ?>> resultRecipes = map.computeIfAbsent(recipeMapId, k -> new ArrayList<>());

            try {
                try(Resource resource = manager.getResource(resourcePath)) {
                    InputStream inputStream = resource.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                    JsonElement jsonElement = JsonHelper.deserialize(GSON, reader, JsonElement.class);
                    MachineRecipe<?, ?> machineRecipe = parseRecipe(recipeId, jsonElement);
                    resultRecipes.add(machineRecipe);
                }
            } catch (Exception exception) {
                LOGGER.error("Failed to load machine recipe {}", resourcePath, exception);
            }
        }
        return map;
    }

    @Override
    protected void apply(Map<String, List<MachineRecipe<?, ?>>> prepared, ResourceManager manager, Profiler profiler) {
        Map<String, MachineRecipeType> recipeTypeIndex = buildRecipeTypeIndex();
        this.recipes.clear();

        for (Map.Entry<String, List<MachineRecipe<?, ?>>> pair : prepared.entrySet()) {
            String recipeMapName = pair.getKey();
            MachineRecipeType machineRecipeType = recipeTypeIndex.get(recipeMapName);

            if (machineRecipeType == null) {
                LOGGER.error("Failed to find recipe map by name {}. Recipes associated with it will be ignored ({} recipes)", recipeMapName, pair.getValue().size());
                continue;
            }

            RecipeMap recipeMap = new RecipeMap(machineRecipeType);
            recipeMap.appendRecipes(pair.getValue());
            if (recipeManager != null) {
                recipeMap.applyVanillaRecipes(this.recipeManager);
            }

            LOGGER.info("Loaded {} recipes into the recipe map {}", pair.getValue().size(), recipeMap);
            this.recipes.put(machineRecipeType, recipeMap);
        }
    }

    public void setRecipes(Map<MachineRecipeType, List<MachineRecipe<?, ?>>> recipes) {
        this.recipes.clear();

        for (MachineRecipeType recipeType : recipes.keySet()) {
            RecipeMap recipeMap = new RecipeMap(recipeType);
            recipeMap.appendRecipes(recipes.get(recipeType));

            this.recipes.put(recipeType, recipeMap);
        }
    }
}
