package gregtech.api.util.ref;

import alexiil.mc.lib.attributes.misc.Reference;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class InventorySlotRef implements Reference<ItemStack> {

    private final Inventory inventory;
    private final int slotIndex;

    protected InventorySlotRef(Inventory inventory, int slotIndex) {
        this.inventory = inventory;
        this.slotIndex = slotIndex;
    }

    public static Reference<ItemStack> of(Inventory inventory, int slot) {
        return new InventorySlotRef(inventory, slot);
    }

    @Override
    public ItemStack get() {
        return this.inventory.getStack(slotIndex);
    }

    @Override
    public boolean set(ItemStack value) {
        this.inventory.setStack(slotIndex, value);
        return true;
    }

    @Override
    public boolean isValid(ItemStack value) {
        return this.inventory.isValid(slotIndex, value);
    }
}
