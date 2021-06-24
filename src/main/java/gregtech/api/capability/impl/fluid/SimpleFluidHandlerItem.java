package gregtech.api.capability.impl.fluid;

import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.item.ItemBasedSingleFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.AbstractItemBasedAttribute;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class SimpleFluidHandlerItem extends ItemBasedSingleFluidInv {

    private static final String TAG = "GT.Fluid";
    protected final FluidAmount capacity;

    public SimpleFluidHandlerItem(Reference<ItemStack> stackRef, LimitedConsumer<ItemStack> excessStacks, FluidAmount capacity) {
        super(stackRef, excessStacks);
        this.capacity = capacity;
    }

    @Override
    protected HeldFluidInfo getInfo(ItemStack stack) {
        NbtCompound fluidTag = stack.getSubTag(TAG);
        if (fluidTag == null) {
            return new HeldFluidInfo(FluidVolumeUtil.EMPTY, capacity);
        }

        FluidVolume containedFluid = FluidVolume.fromTag(fluidTag);
        return new HeldFluidInfo(containedFluid, capacity);
    }

    @Override
    protected ItemStack writeToStack(ItemStack stack, FluidVolume fluid) {
        if (fluid.isEmpty()) {
            stack.removeSubTag(TAG);
            return stack;
        }

        NbtCompound fluidTag = stack.getOrCreateSubTag(TAG);
        fluid.toTag(fluidTag);
        return stack;
    }

    @Override
    protected boolean isInvalid(ItemStack stack) {
        return false;
    }
}
