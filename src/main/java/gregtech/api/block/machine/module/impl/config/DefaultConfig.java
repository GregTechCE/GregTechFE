package gregtech.api.block.machine.module.impl.config;

import gregtech.api.block.machine.module.MachineModuleConfig;

public class DefaultConfig implements MachineModuleConfig {

    private DefaultConfig() {
    }

    public static final class Builder extends ConfigurationBuilder {

        public static Builder start() {
            return new Builder();
        }

        @Override
        public Builder copy() {
            return new Builder();
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
        }

        @Override
        public DefaultConfig build() {
            return new DefaultConfig();
        }
    }
}
