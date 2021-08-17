package gregtech.api.module.impl.config;

import gregtech.api.module.MachineModuleConfig;

public class OrientationConfig implements MachineModuleConfig {

    private final boolean supportsVerticalOrientation;

    protected OrientationConfig(boolean supportsVerticalOrientation) {
        this.supportsVerticalOrientation = supportsVerticalOrientation;
    }

    public boolean supportsVerticalOrientation() {
        return supportsVerticalOrientation;
    }

    public static class Builder extends ConfigurationBuilder {

        private boolean supportsVerticalOrientation;

        public Builder start() {
            return new Builder();
        }

        public Builder supportsVerticalOrientation() {
            this.supportsVerticalOrientation = true;
            return this;
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            if (builder instanceof Builder other) {
                this.supportsVerticalOrientation = other.supportsVerticalOrientation;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public OrientationConfig build() {
            return new OrientationConfig(this.supportsVerticalOrientation);
        }
    }
}
