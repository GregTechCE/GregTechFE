package gregtech.api.gui.widgets.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.awt.*;

class WidgetSlot extends Slot implements Scissored {

    private final SlotWidget slotWidget;

    public WidgetSlot(SlotWidget slotWidget, Inventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
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

    @Override
    public Rectangle getScissor() {
        return slotWidget.scissor;
    }
}
