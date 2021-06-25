package gregtech.api.unification.material;

import gregtech.api.unification.material.flags.MaterialFlag;
import gregtech.api.unification.material.properties.MaterialProperty;
import gregtech.api.util.registry.AlreadyRegisteredKeyException;
import gregtech.api.util.registry.GTRegistry;
import gregtech.api.util.registry.GTRegistryKey;

import java.util.*;

public class GTMaterial implements GTRegistryKey {
    private final String name;
    private final Set<MaterialFlag> flags = new HashSet<>();
    private final Map<MaterialProperty<?>, Object> properties = new HashMap<>();

    private GTMaterial(String name, Set<MaterialFlag> flags, Map<MaterialProperty<?>, Object> properties) {
        this.name = name;
        this.flags.addAll(flags);
        this.properties.putAll(properties);
    }

    @Override
    public String getKey() {
        return name;
    }


    public static class Settings {
        private static final GTRegistry<GTMaterial> registry = new GTRegistry<>();

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

            var gtMaterial = new GTMaterial(name, flags, properties);

            try {
                registry.put(gtMaterial);
            } catch (AlreadyRegisteredKeyException e) {
                //TODO: Log
                return null;
            }

            return gtMaterial;
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
