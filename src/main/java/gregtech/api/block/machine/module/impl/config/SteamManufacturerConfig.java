package gregtech.api.block.machine.module.impl.config;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import com.google.common.base.Preconditions;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.archetype.IOInventoryModule;
import gregtech.api.recipe.MachineRecipeType;
import gregtech.api.util.VoltageTier;

public class SteamManufacturerConfig extends ManufacturerConfig {

    private final FluidKey steamFluid;
    private final FluidAmount steamTankCapacity;
    private final VoltageTier maxVoltage;

    private final double durationMultiplier;
    private final FluidAmount steamPerEnergyUnit;

    protected SteamManufacturerConfig(MachineModuleType<?, ? extends IOInventoryModule> inventoryModule, MachineRecipeType recipeType, FluidKey steamFluid, FluidAmount steamTankCapacity, VoltageTier maxVoltage, double durationMultiplier, FluidAmount steamPerEnergyUnit) {
        super(inventoryModule, recipeType);

        Preconditions.checkNotNull(steamFluid, "steamFluid");
        Preconditions.checkNotNull(steamTankCapacity, "steamTankCapacity");
        Preconditions.checkNotNull(maxVoltage, "maxVoltage");
        Preconditions.checkNotNull(steamPerEnergyUnit, "steamPerEnergyUnit");

        this.steamFluid = steamFluid;
        this.steamTankCapacity = steamTankCapacity;
        this.maxVoltage = maxVoltage;
        this.durationMultiplier = durationMultiplier;
        this.steamPerEnergyUnit = steamPerEnergyUnit;
    }

    public FluidKey getSteamFluid() {
        return steamFluid;
    }

    public FluidAmount getSteamTankCapacity() {
        return steamTankCapacity;
    }

    public VoltageTier getMaxVoltage() {
        return maxVoltage;
    }

    public double getDurationMultiplier() {
        return durationMultiplier;
    }

    public FluidAmount getSteamPerEnergyUnit() {
        return steamPerEnergyUnit;
    }

    public static class Builder extends ManufacturerConfig.Builder {

        private FluidKey steamFluid;
        private FluidAmount steamTankCapacity;
        private VoltageTier maxVoltage;
        private double durationMultiplier = 1.0;
        private FluidAmount steamPerEnergyUnit;

        public static Builder start() {
            return new Builder();
        }

        public Builder steamFluid(FluidKey steamFluid) {
            Preconditions.checkNotNull(steamFluid, "steamFluid");
            this.steamFluid = steamFluid;
            return this;
        }

        public Builder steamTankCapacity(FluidAmount steamTankCapacity) {
            Preconditions.checkNotNull(steamTankCapacity, "steamTankCapacity");
            this.steamTankCapacity = steamTankCapacity;
            return this;
        }

        public Builder maxVoltage(VoltageTier maxVoltage) {
            Preconditions.checkNotNull(maxVoltage, "maxVoltage");
            this.maxVoltage = maxVoltage;
            return this;
        }

        public Builder durationMultiplier(double durationMultiplier) {
            Preconditions.checkArgument(durationMultiplier > 0, "durationMultiplier should be positive");
            this.durationMultiplier = durationMultiplier;
            return this;
        }

        public Builder steamPerEnergyUnit(FluidAmount steamPerEnergyUnit) {
            Preconditions.checkNotNull(steamPerEnergyUnit, "steamPerEnergyUnit");
            this.steamPerEnergyUnit = steamPerEnergyUnit;
            return this;
        }

        @Override
        public Builder inventory(MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType) {
            return (Builder) super.inventory(inventoryModuleType);
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            super.copyPropertiesFrom(builder);
            if (builder instanceof Builder other) {
                this.steamFluid = other.steamFluid;
                this.steamTankCapacity = other.steamTankCapacity;
                this.maxVoltage = other.maxVoltage;
                this.durationMultiplier = other.durationMultiplier;
                this.steamPerEnergyUnit = other.steamPerEnergyUnit;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public SteamManufacturerConfig build() {
            return new SteamManufacturerConfig(this.inventoryModuleType, this.recipeType, this.steamFluid, this.steamTankCapacity, this.maxVoltage, this.durationMultiplier, this.steamPerEnergyUnit);
        }
    }
}
