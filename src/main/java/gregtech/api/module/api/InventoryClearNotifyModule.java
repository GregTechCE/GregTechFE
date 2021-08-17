package gregtech.api.module.api;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface InventoryClearNotifyModule {

    void clearInventory(List<ItemStack> droppedItems, Simulation simulation);

    static void clearInventory(FixedItemInv inventory, List<ItemStack> droppedItems, Simulation simulation) {
        for (int slot = 0; slot < inventory.getSlotCount(); slot++) {
            ItemStack itemStack = inventory.getInvStack(slot);

            if (!itemStack.isEmpty()) {
                if (inventory.setInvStack(slot, ItemStack.EMPTY, simulation)) {
                    droppedItems.add(itemStack.copy());
                }
            }
        }
    }
}
