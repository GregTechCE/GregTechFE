package gregtech.api.capability.impl.fluid;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Reference;
import net.minecraft.item.ItemStack;

public class FullOrEmptyFluidHandlerItem extends SimpleFluidHandlerItem {

    public FullOrEmptyFluidHandlerItem(Reference<ItemStack> stackRef, LimitedConsumer<ItemStack> excessStacks, FluidAmount capacity) {
        super(stackRef, excessStacks, capacity);
    }

    @Override
    public FluidVolume attemptInsertion(FluidVolume fluid, Simulation simulation) {
        if (fluid.amount().isLessThan(capacity)) {
            return fluid;
        }
        return super.attemptInsertion(fluid, simulation);
    }

    @Override
    public FluidVolume attemptExtraction(FluidFilter filter, FluidAmount maxAmount, Simulation simulation) {
        if (maxAmount.isLessThan(capacity)) {
            return FluidVolumeUtil.EMPTY;
        }
        return super.attemptExtraction(filter, maxAmount, simulation);
    }

    @Override
    public FluidAmount getMinimumAcceptedAmount() {
        return capacity;
    }
}
