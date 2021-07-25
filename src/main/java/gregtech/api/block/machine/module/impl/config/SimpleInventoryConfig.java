package gregtech.api.block.machine.module.impl.config;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.EmptyFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.impl.DirectFixedItemInv;
import alexiil.mc.lib.attributes.item.impl.EmptyFixedItemInv;
import com.google.common.base.Preconditions;
import gregtech.api.block.machine.module.MachineModuleConfig;

public class SimpleInventoryConfig implements MachineModuleConfig {

    private final int importInventorySize;
    private final int exportInventorySize;

    private final int importFluidTankCount;
    private final int exportFluidTankCount;
    private final FluidAmount fluidTankCapacity;

    protected SimpleInventoryConfig(int importInventorySize, int exportInventorySize, int importFluidTankCount, int exportFluidTankCount, FluidAmount fluidTankCapacity) {
        Preconditions.checkArgument(fluidTankCapacity != null || (importFluidTankCount == 0 && exportFluidTankCount == 0),
                "fluidTankCapacity must be set when either importFluidTankCount or exportFluidTankCount is nonzero");
        this.importInventorySize = importInventorySize;
        this.exportInventorySize = exportInventorySize;
        this.importFluidTankCount = importFluidTankCount;
        this.exportFluidTankCount = exportFluidTankCount;
        this.fluidTankCapacity = fluidTankCapacity;
    }

    public FixedItemInv createItemImportInventory() {
        if (this.importInventorySize > 0) {
            return new DirectFixedItemInv(this.importInventorySize);
        }
        return EmptyFixedItemInv.INSTANCE;
    }

    public FixedItemInv createItemExportInventory() {
        if (this.exportInventorySize > 0) {
            return new DirectFixedItemInv(this.exportInventorySize);
        }
        return EmptyFixedItemInv.INSTANCE;
    }

    public FixedFluidInv createFluidImportInventory() {
        if (this.importFluidTankCount > 0) {
            return new SimpleFixedFluidInv(this.importFluidTankCount, this.fluidTankCapacity);
        }
        return EmptyFixedFluidInv.INSTANCE;
    }

    public FixedFluidInv createFluidExportInventory() {
        if (this.exportFluidTankCount > 0) {
            return new SimpleFixedFluidInv(this.exportFluidTankCount, this.fluidTankCapacity);
        }
        return EmptyFixedFluidInv.INSTANCE;
    }

    public static class Builder extends ConfigurationBuilder {

        private int importInventorySize;
        private int exportInventorySize;
        private int importFluidTankCount;
        private int exportFluidTankCount;
        private FluidAmount fluidTankCapacity;

        public Builder start() {
            return new Builder();
        }

        public Builder importInventorySize(int importInventorySize) {
            Preconditions.checkArgument(importInventorySize > 0, "importInventorySize must be positive");
            this.importInventorySize = importInventorySize;
            return this;
        }

        public Builder exportInventorySize(int exportInventorySize) {
            Preconditions.checkArgument(exportInventorySize > 0, "exportInventorySize must be positive");
            this.exportInventorySize = exportInventorySize;
            return this;
        }

        public Builder importFluidTankCount(int importFluidTankCount) {
            Preconditions.checkArgument(importFluidTankCount > 0, "importFluidTankCount should be positive");
            this.importFluidTankCount = importFluidTankCount;
            return this;
        }

        public Builder exportFluidTankCount(int exportFluidTankCount) {
            Preconditions.checkArgument(exportFluidTankCount > 0, "exportFluidTankCount should be positive");
            this.exportFluidTankCount = exportFluidTankCount;
            return this;
        }

        public Builder fluidTankCapacity(FluidAmount fluidTankCapacity) {
            Preconditions.checkNotNull(fluidTankCapacity, "fluidTankCapacity");
            this.fluidTankCapacity = fluidTankCapacity;
            return this;
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            if (builder instanceof Builder other) {
                this.importInventorySize = other.importInventorySize;
                this.exportInventorySize = other.exportInventorySize;
                this.importFluidTankCount = other.importFluidTankCount;
                this.exportFluidTankCount = other.exportFluidTankCount;
                this.fluidTankCapacity = other.fluidTankCapacity;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public SimpleInventoryConfig build() {
            return new SimpleInventoryConfig(this.importInventorySize, this.exportInventorySize, this.importFluidTankCount, this.exportFluidTankCount, this.fluidTankCapacity);
        }
    }
}
