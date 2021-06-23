package gregtech.api.gui.widgets;

import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.gui.widgets.slot.SlotWidget;
import net.minecraft.item.ItemStack;

public class FluidContainerSlotWidget extends SlotWidget {

    private final boolean requireFilledContainer;

    public FluidContainerSlotWidget(FixedItemInv itemHandler, int slotIndex, int xPosition, int yPosition, boolean requireFilledContainer) {
        super(itemHandler, slotIndex, xPosition, yPosition, true, true);
        this.requireFilledContainer = requireFilledContainer;
    }

    @Override
    public boolean canPutStack(ItemStack stack) {
        if (requireFilledContainer) {
            FluidExtractable extractable = FluidAttributes.EXTRACTABLE.get(stack);
            return extractable.couldExtractAnything();
        }
        FluidInsertable insertable = FluidAttributes.INSERTABLE.get(stack);
        return !(insertable instanceof NullVariant);
    }
}
