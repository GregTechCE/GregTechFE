package gregtech.api.gui.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

class EmptySlotPlaceholder extends Slot {

    private static final Inventory EMPTY_INVENTORY = new SimpleInventory(ItemStack.EMPTY);

    public EmptySlotPlaceholder() {
        super(EMPTY_INVENTORY, 0, -100000, -100000);
    }

    @Override
    public ItemStack getStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return false;
    }

    @Override
    public int getMaxItemCount() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
