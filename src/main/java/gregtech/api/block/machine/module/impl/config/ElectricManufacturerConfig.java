package gregtech.api.block.machine.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.archetype.EnergyContainerModule;
import gregtech.api.block.machine.module.impl.archetype.IOInventoryModule;
import gregtech.api.recipe.MachineRecipeType;

public class ElectricManufacturerConfig extends ManufacturerConfig {

    private final MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModule;

    protected ElectricManufacturerConfig(MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModule, MachineModuleType<?, ? extends IOInventoryModule> inventoryModule, MachineRecipeType recipeType) {
        super(inventoryModule, recipeType);
        this.energyContainerModule = energyContainerModule;
    }

    public MachineModuleType<?, ? extends EnergyContainerModule> getEnergyContainerModule() {
        return energyContainerModule;
    }

    public static class Builder extends ManufacturerConfig.Builder {

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
        public Builder recipeType(MachineRecipeType recipeType) {
            return (Builder) super.recipeType(recipeType);
        }

        @Override
        public Builder inventory(MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType) {
            return (Builder) super.inventory(inventoryModuleType);
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            super.copyPropertiesFrom(builder);
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
        public ElectricManufacturerConfig build() {
            return new ElectricManufacturerConfig(this.energyContainerModuleType, this.inventoryModuleType, this.recipeType);
        }
    }
}
