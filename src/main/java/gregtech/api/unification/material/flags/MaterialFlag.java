package gregtech.api.unification.material.flags;

import gregtech.api.unification.material.properties.MaterialProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaterialFlag {
    private final String name;
    private final List<MaterialFlag> requiredFlags = new ArrayList<>();
    private final List<MaterialFlag> conflictingFlags = new ArrayList<>();
    private final List<MaterialProperty<?>> requiredProperties = new ArrayList<>();

    public MaterialFlag(String name, List<MaterialFlag> requiredFlags, List<MaterialFlag> conflictingFlags, List<MaterialProperty<?>> requiredProperties) {
        this.name = name;
        this.requiredFlags.addAll(requiredFlags);
        this.conflictingFlags.addAll(conflictingFlags);
        this.requiredProperties.addAll(requiredProperties);
    }


    public static class Builder {
        private final String name;
        private final List<MaterialFlag> requiredFlags = new ArrayList<>();
        private final List<MaterialFlag> conflictingFlags = new ArrayList<>();
        private final List<MaterialProperty<?>> requiredProperties = new ArrayList<>();

        public Builder(String name) {
            this.name = name;
        }

        public MaterialFlag.Builder requires(MaterialFlag materialFlag) {
            this.requiredFlags.add(materialFlag);
            return this;
        }

        public MaterialFlag.Builder conflictsWith(MaterialFlag materialFlag) {
            this.conflictingFlags.add(materialFlag);
            return this;
        }

        public MaterialFlag.Builder requiresProperty(MaterialProperty<?> property) {
            this.requiredProperties.add(property);
            return this;
        }

        public MaterialFlag build() throws MaterialFlagValidationException {
            validate();

            return new MaterialFlag(name, requiredFlags, conflictingFlags, requiredProperties);
        }

        private void validate() throws MaterialFlagValidationException {
            if (!Collections.disjoint(requiredFlags, conflictingFlags))
                throw new MaterialFlagValidationException("Required flags and conflicts flags has same element");
        }
    }
}
