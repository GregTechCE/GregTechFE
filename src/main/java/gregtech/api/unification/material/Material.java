package gregtech.api.unification.material;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import gregtech.api.GTValues;
import gregtech.api.unification.material.flags.MaterialFlag;
import gregtech.api.unification.material.flags.MaterialProperty;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class Material implements Comparable<Material> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Material.class);
    private static final boolean MATERIAL_ERRORS_ARE_FATAL = true;

    public static final Registry<Material> REGISTRY = FabricRegistryBuilder
            .createSimple(Material.class, new Identifier(GTValues.MODID, "materials"))
            .buildAndRegister();

    private final Set<MaterialFlag> materialFlags;
    private final Map<MaterialProperty<?>, Object> properties;

    public Material(Settings settings) {
        this.materialFlags = ImmutableSet.copyOf(settings.materialFlags);
        this.properties = ImmutableMap.copyOf(settings.properties);
    }

    private boolean verifyMaterial() {
        boolean isMaterialValid = true;

        for (MaterialFlag flag : materialFlags) {
            isMaterialValid &= flag.verifyMaterialFlags(this);
        }
        for (MaterialProperty<?> property : properties.keySet()) {
            isMaterialValid &= property.verifyPropertyValue(this, properties.get(property));
        }
        if (!isMaterialValid) {
            LOGGER.error("Material {} is not valid. See above messages for errors", this);
            return false;
        }
        return true;
    }

    public Identifier getName() {
        return Preconditions.checkNotNull(REGISTRY.getId(this));
    }

    public String getTranslationKey() {
        Identifier id = REGISTRY.getId(this);
        Preconditions.checkNotNull(id);
        return "material." + id.getNamespace() + "." + id.getPath();
    }

    public Text getDisplayName() {
        return new TranslatableText(getTranslationKey());
    }

    public boolean hasFlag(MaterialFlag flag) {
        return this.materialFlags.contains(flag);
    }

    public <T> Optional<T> queryProperty(MaterialProperty<T> property) {
        Object propertyValue = this.properties.get(property);
        if (propertyValue == null) {
            return Optional.ofNullable(property.getDefaultValue());
        }
        return Optional.of(property.cast(propertyValue));
    }

    public <T> T queryPropertyChecked(MaterialProperty<T> property) {
        return queryProperty(property).orElseThrow(() -> {
            String errorMessage = String.format("Material %s does not have a property %s", this, property);
            return new IllegalArgumentException(errorMessage);
        });
    }

    @Override
    public int compareTo(Material material) {
        return toString().compareTo(material.toString());
    }

    @Override
    public String toString() {
        Identifier identifier = REGISTRY.getId(this);
        return String.valueOf(identifier);
    }

    /** Verifies all material flags and values in registry */
    public static void verifyMaterialRegistry() {
        int invalidMaterialsFound = 0;

        for (Identifier materialId : REGISTRY.getIds()) {
            Material material = Preconditions.checkNotNull(REGISTRY.get(materialId));
            if (!material.verifyMaterial()) {
                invalidMaterialsFound++;
            }
        }

        if (invalidMaterialsFound > 0) {
            LOGGER.error("Found {} invalid materials, see messages above for errors", invalidMaterialsFound);

            if (MATERIAL_ERRORS_ARE_FATAL) {
                LOGGER.error("Loading cannot continue");
                throw new IllegalStateException("Material registry is in invalid state");
            }
        }
    }

    public static class Settings {
        private static final String FLAG_PROPERTY_ERROR_MESSAGE =
                "Cannot use .flag(MaterialFlag) to add material properties, use .property(MaterialProperty, T) instead!";
        private final Set<MaterialFlag> materialFlags = new HashSet<>();
        private final Map<MaterialProperty<?>, Object> properties = new HashMap<>();

        public Settings flag(MaterialFlag flag) {
            Preconditions.checkNotNull(flag, "flag");
            Preconditions.checkArgument(!(flag instanceof MaterialProperty<?>), FLAG_PROPERTY_ERROR_MESSAGE);

            this.materialFlags.add(flag);
            return this;
        }

        @Deprecated
        public void flag(MaterialProperty<?> property) {
            throw new IllegalArgumentException(FLAG_PROPERTY_ERROR_MESSAGE);
        }

        public <T> Settings property(MaterialProperty<T> property, T value) {
            Preconditions.checkNotNull(property, "property");
            Preconditions.checkNotNull(value, "value");

            this.materialFlags.add(property);
            this.properties.put(property, value);
            return this;
        }
    }
}
