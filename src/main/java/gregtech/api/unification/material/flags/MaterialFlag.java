package gregtech.api.unification.material.flags;

import gregtech.api.GTValues;
import gregtech.api.unification.material.properties.MaterialProperty;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MaterialFlag {
    private final Set<MaterialFlag> requiredFlags = new HashSet<>();
    private final Set<MaterialFlag> conflictingFlags = new HashSet<>();
    private final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

    private MaterialFlag(Set<MaterialFlag> requiredFlags, Set<MaterialFlag> conflictingFlags, Set<MaterialProperty<?>> requiredProperties) {
        this.requiredFlags.addAll(requiredFlags);
        this.conflictingFlags.addAll(conflictingFlags);
        this.requiredProperties.addAll(requiredProperties);
    }

    public Set<MaterialFlag> getRequiredFlags() {
        return Collections.unmodifiableSet(requiredFlags);
    }

    public Set<MaterialFlag> getConflictingFlags() {
        return Collections.unmodifiableSet(conflictingFlags);
    }

    @SuppressWarnings("java:S1452")
    public Set<MaterialProperty<?>> getRequiredProperties() {
        return Collections.unmodifiableSet(requiredProperties);
    }


    public static class Builder {
        public static final Registry<MaterialFlag> REGISTRY =
                FabricRegistryBuilder.createSimple(MaterialFlag.class,
                        new Identifier(GTValues.MODID, "material_flag")).buildAndRegister();

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

            var materialFlag = new MaterialFlag(requiredFlags, conflictingFlags, requiredProperties);

            return Registry.register(REGISTRY, new Identifier(GTValues.MODID, name), materialFlag);
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
