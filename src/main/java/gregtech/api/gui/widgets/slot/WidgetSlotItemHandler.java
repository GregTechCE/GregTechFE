package gregtech.api.gui.widgets.slot;

import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.compat.SlotFixedItemInv;
import gregtech.api.util.EmptyScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.awt.*;

class WidgetSlotItemHandler extends SlotFixedItemInv {

    private final SlotWidget slotWidget;

    //TODO server/container parameters are not really valid, but we just do not have that information available here
    public WidgetSlotItemHandler(SlotWidget slotWidget, FixedItemInv itemHandler, int index, int xPosition, int yPosition) {
        super(new EmptyScreenHandler(), itemHandler, true, index, xPosition, yPosition);
        this.slotWidget = slotWidget;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return slotWidget.canPutStack(stack) && super.canInsert(stack);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerIn) {
        return slotWidget.canTakeStack(playerIn) && super.canTakeItems(playerIn);
    }

    @Override
    public void onTakeItem(PlayerEntity thePlayer, ItemStack stack) {
        super.onTakeItem(thePlayer, stack);
        slotWidget.onItemTake(thePlayer, stack, false);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        slotWidget.onSlotChanged();
    }

    @Override
    public boolean isEnabled() {
        return slotWidget.isEnabled();
    }
}
