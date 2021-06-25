package gregtech.api.unification.material;

import gregtech.api.GTValues;
import gregtech.api.unification.material.flags.MaterialFlag;
import gregtech.api.unification.material.properties.MaterialProperty;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class GTMaterial {
    private Set<MaterialFlag> flags = new HashSet<>();
    private Map<MaterialProperty<?>, Object> properties = new HashMap<>();

    private GTMaterial() {
    }

    public static class Settings {
        public static final Registry<GTMaterial> REGISTRY =
                FabricRegistryBuilder.createSimple(GTMaterial.class, new Identifier(GTValues.MODID, "material"))
                        .buildAndRegister();


        private final String name;
        private final Set<MaterialFlag> flags = new HashSet<>();
        private final Set<MaterialFlag> conflictingFlags = new HashSet<>();
        private final Map<MaterialProperty<?>, Object> properties = new HashMap<>();
        private final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

        public Settings(String name) {
            this.name = name;
        }

        public GTMaterial.Settings add(MaterialFlag materialFlag) {
            addFlagAndRequiredFlags(materialFlag);
            addConflictingFlags(materialFlag.getConflictingFlags());
            addRequiredProperties(materialFlag.getRequiredProperties());

            return this;
        }

        public <T> GTMaterial.Settings add(MaterialProperty<T> materialProperty, T value) {
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
        
        public GTMaterial build() {
            if (!validate()) {
                return null;
            }

            var gtMaterial = new GTMaterial();
            gtMaterial.properties = this.properties;
            gtMaterial.flags = this.flags;

            return Registry.register(REGISTRY, new Identifier(GTValues.MODID, name), gtMaterial);
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
