package gregtech.api.util.ref;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.misc.Reference;
import net.minecraft.item.ItemStack;

public class FixedInvSlotRef implements Reference<ItemStack> {

    private final FixedItemInv inventory;
    private final int slotIndex;

    protected FixedInvSlotRef(FixedItemInv inventory, int slotIndex) {
        this.inventory = inventory;
        this.slotIndex = slotIndex;
    }

    public static Reference<ItemStack> of(FixedItemInv inventory, int slot) {
        return new FixedInvSlotRef(inventory, slot);
    }

    @Override
    public ItemStack get() {
        return this.inventory.getInvStack(slotIndex);
    }

    @Override
    public boolean set(ItemStack value) {
        return this.inventory.setInvStack(slotIndex, value, Simulation.ACTION);
    }

    @Override
    public boolean isValid(ItemStack value) {
        return this.inventory.setInvStack(slotIndex, value, Simulation.SIMULATE);
    }
}
