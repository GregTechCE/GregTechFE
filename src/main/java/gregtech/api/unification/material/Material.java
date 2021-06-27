package gregtech.api.unification.material;

import gregtech.api.GTValues;
import gregtech.api.unification.material.flags.MaterialFlag;
import gregtech.api.unification.material.properties.MaterialProperty;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class Material {
    private Set<MaterialFlag> flags = new HashSet<>();
    private Map<MaterialProperty<?>, Object> properties = new HashMap<>();

    private Material() {
    }

    public boolean hasFlag(MaterialFlag flag) {
        return flags.contains(flag);
    }

    public Set<MaterialFlag> getFlags(){
        return Collections.unmodifiableSet(flags);
    }

    public boolean hasProperty(MaterialProperty<?> property){
        return properties.containsKey(property);
    }

    public <T> T getPropertyValue(MaterialProperty<T> property){
        return property.cast(properties.get(property));
    }

    public static class Settings {
        public static final Registry<Material> REGISTRY =
                FabricRegistryBuilder.createSimple(Material.class, new Identifier(GTValues.MODID, "material"))
                        .buildAndRegister();


        private final String name;
        private final Set<MaterialFlag> flags = new HashSet<>();
        private final Set<MaterialFlag> conflictingFlags = new HashSet<>();
        private final Map<MaterialProperty<?>, Object> properties = new HashMap<>();
        private final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

        public Settings(String name) {
            this.name = name;
        }

        public Material.Settings addFlag(MaterialFlag materialFlag) {
            addFlagAndRequiredFlags(materialFlag);
            addConflictingFlags(materialFlag.getConflictingFlags());
            addRequiredProperties(materialFlag.getRequiredProperties());

            return this;
        }

        public <T> Material.Settings addProperty(MaterialProperty<T> materialProperty, T value) {
            this.properties.put(materialProperty, value);

            addRequiredProperties(materialProperty.getRequiredProperties());
            addFlagsAndRequiredFlags(materialProperty.getRequiredFlags());

            return this;
        }

        private void addConflictingFlags(Set<MaterialFlag> flags) {
            for (MaterialFlag flag : flags) {
                //as flags are checked only if added successfully circular dependency is not possible
                if (this.conflictingFlags.add(flag)) {
                    addConflictingFlags(flag.getConflictingFlags());
                }
            }
        }

        private void addFlagsAndRequiredFlags(Set<MaterialFlag> flags) {
            for (MaterialFlag flag : flags) {
                addFlagAndRequiredFlags(flag);
            }
        }

        private void addFlagAndRequiredFlags(MaterialFlag flag) {
            if (this.flags.add(flag)) {
                addFlagsAndRequiredFlags(flag.getRequiredFlags());
                addRequiredProperties(flag.getRequiredProperties());
            }
        }

        private void addRequiredProperties(Set<MaterialProperty<?>> materialProperties) {
            for (MaterialProperty<?> materialProperty : materialProperties) {
                addRequiredProperty(materialProperty);
            }
        }

        private void addRequiredProperty(MaterialProperty<?> materialProperty) {
            if (this.requiredProperties.add(materialProperty)) {
                addRequiredProperties(materialProperty.getRequiredProperties());
                addFlagsAndRequiredFlags(materialProperty.getRequiredFlags());
            }
        }

        public Material build() {
            if (!validate()) {
                return null;
            }

            var material = new Material();
            material.properties = this.properties;
            material.flags = this.flags;

            return Registry.register(REGISTRY, new Identifier(GTValues.MODID, name), material);
        }

        private boolean validate() {
            if (!Collections.disjoint(flags, conflictingFlags)) {
                //TODO: log error
                return false;
            }

            if (!properties.keySet().containsAll(requiredProperties)) {
                //TODO: log error
                return false;
            }

            return true;
        }

    }
}
