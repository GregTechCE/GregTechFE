package gregtech.api.block.machine.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.block.machine.module.MachineModuleConfig;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.archetype.EnergyContainerModule;

public class EnergyContainerTypeConfig implements MachineModuleConfig {

    private final MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModuleType;

    protected EnergyContainerTypeConfig(MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModuleType) {
        Preconditions.checkNotNull(energyContainerModuleType, "energyContainerModuleType");
        this.energyContainerModuleType = energyContainerModuleType;
    }

    public MachineModuleType<?, ? extends EnergyContainerModule> getEnergyContainerModuleType() {
        return energyContainerModuleType;
    }

    public static class Builder extends ConfigurationBuilder {

        protected MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModuleType;

        public static Builder start() {
            return new Builder();
        }

        public Builder energyContainer(MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModuleType) {
            Preconditions.checkNotNull(energyContainerModuleType, "energyContainerModuleType");
            this.energyContainerModuleType = energyContainerModuleType;
            return this;
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            if (builder instanceof Builder other) {
                this.energyContainerModuleType = other.energyContainerModuleType;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public EnergyContainerTypeConfig build() {
            return new EnergyContainerTypeConfig(this.energyContainerModuleType);
        }
    }
}
