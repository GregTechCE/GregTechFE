package gregtech.api.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.module.MachineModuleType;
import gregtech.api.module.impl.archetype.EnergyContainerModule;
import gregtech.api.module.impl.archetype.IOInventoryModule;
import gregtech.api.recipe.MachineRecipeType;

public class GeneratorConfig extends ManufacturerConfig {

    private final MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModuleType;
    private final boolean voidExcessiveEnergy;

    public GeneratorConfig(MachineModuleType<?, ? extends IOInventoryModule> inventoryModule, MachineRecipeType recipeType, MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModuleType, boolean voidExcessiveEnergy) {
        super(inventoryModule, recipeType);
        Preconditions.checkNotNull(energyContainerModuleType, "energyContainerModuleType");
        this.energyContainerModuleType = energyContainerModuleType;
        this.voidExcessiveEnergy = voidExcessiveEnergy;
    }

    public boolean isVoidExcessiveEnergy() {
        return voidExcessiveEnergy;
    }

    public MachineModuleType<?, ? extends EnergyContainerModule> getEnergyContainerModuleType() {
        return energyContainerModuleType;
    }

    public static class Builder extends ManufacturerConfig.Builder {

        private MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModuleType;
        private boolean voidExcessiveEnergy;

        public static Builder start() {
            return new Builder();
        }

        public Builder energyContainer(MachineModuleType<?, ? extends EnergyContainerModule> energyContainerModuleType) {
            Preconditions.checkNotNull(energyContainerModuleType, "energyContainerModuleType");
            this.energyContainerModuleType = energyContainerModuleType;
            return this;
        }

        public Builder voidExcessiveEnergy() {
            this.voidExcessiveEnergy = true;
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
                this.voidExcessiveEnergy = other.voidExcessiveEnergy;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public GeneratorConfig build() {
            return new GeneratorConfig(this.inventoryModuleType, this.recipeType, this.energyContainerModuleType, this.voidExcessiveEnergy);
        }
    }
}
