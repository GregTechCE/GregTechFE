package gregtech.api.unification.material.flags;

import gregtech.api.unification.material.properties.MaterialProperty;
import gregtech.api.util.registry.AlreadyRegisteredKeyException;
import gregtech.api.util.registry.GTRegistry;
import gregtech.api.util.registry.GTRegistryKey;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MaterialFlag implements GTRegistryKey {
    private final String name;
    private final Set<MaterialFlag> requiredFlags = new HashSet<>();
    private final Set<MaterialFlag> conflictingFlags = new HashSet<>();
    private final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

    private MaterialFlag(String name, Set<MaterialFlag> requiredFlags, Set<MaterialFlag> conflictingFlags, Set<MaterialProperty<?>> requiredProperties) {
        this.name = name;
        this.requiredFlags.addAll(requiredFlags);
        this.conflictingFlags.addAll(conflictingFlags);
        this.requiredProperties.addAll(requiredProperties);
    }

    @Override
    public String getKey() {
        return name;
    }


    public static class Builder {
        private static final GTRegistry<MaterialFlag> registry = new GTRegistry<>();

        private final String name;
        private final Set<MaterialFlag> requiredFlags = new HashSet<>();
        private final Set<MaterialFlag> conflictingFlags = new HashSet<>();
        private final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

        public Builder(String name) {
            this.name = name;
        }

        public MaterialFlag.Builder requires(MaterialFlag materialFlag) {
            this.requiredFlags.add(materialFlag);
            return this;
        }

        public MaterialFlag.Builder requires(MaterialProperty<?> property) {
            this.requiredProperties.add(property);
            return this;
        }

        public MaterialFlag.Builder conflictsWith(MaterialFlag materialFlag) {
            this.conflictingFlags.add(materialFlag);
            return this;
        }

        public MaterialFlag build() {
            if (!validate()) {
                return null;
            }

            var materialFlag = new MaterialFlag(name, requiredFlags, conflictingFlags, requiredProperties);

            try {
                registry.put(materialFlag);
            } catch (AlreadyRegisteredKeyException e) {
                //TODO: Log
                return null;
            }

            return materialFlag;
        }

        private boolean validate() {
            if (!Collections.disjoint(requiredFlags, conflictingFlags)) {
                //TODO: log error
                return false;
            }

            return true;
        }
    }
}
