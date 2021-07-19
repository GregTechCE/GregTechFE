package gregtech.api.block.machine.module.impl;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.EmptyFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.impl.DirectFixedItemInv;
import alexiil.mc.lib.attributes.item.impl.EmptyFixedItemInv;
import gregtech.api.block.machine.module.MachineModuleConfig;

public class SimpleInventoryModuleConfig implements MachineModuleConfig {

    private final int importInventorySize;
    private final int exportInventorySize;

    private final int importFluidTankCount;
    private final int exportFluidTankCount;
    private final FluidAmount fluidTankCapacity;

    protected SimpleInventoryModuleConfig(int importInventorySize, int exportInventorySize, int importFluidTankCount, int exportFluidTankCount, FluidAmount fluidTankCapacity) {
        this.importInventorySize = importInventorySize;
        this.exportInventorySize = exportInventorySize;
        this.importFluidTankCount = importFluidTankCount;
        this.exportFluidTankCount = exportFluidTankCount;
        this.fluidTankCapacity = fluidTankCapacity;
    }

    protected FixedItemInv createItemImportInventory() {
        if (this.importInventorySize > 0) {
            return new DirectFixedItemInv(this.importInventorySize);
        }
        return EmptyFixedItemInv.INSTANCE;
    }

    protected FixedItemInv createItemExportInventory() {
        if (this.exportInventorySize > 0) {
            return new DirectFixedItemInv(this.exportInventorySize);
        }
        return EmptyFixedItemInv.INSTANCE;
    }

    protected FixedFluidInv createFluidImportInventory() {
        if (this.importFluidTankCount > 0) {
            return new SimpleFixedFluidInv(this.importFluidTankCount, this.fluidTankCapacity);
        }
        return EmptyFixedFluidInv.INSTANCE;
    }

    protected FixedFluidInv createFluidExportInventory() {
        if (this.exportFluidTankCount > 0) {
            return new SimpleFixedFluidInv(this.exportFluidTankCount, this.fluidTankCapacity);
        }
        return EmptyFixedFluidInv.INSTANCE;
    }
}
