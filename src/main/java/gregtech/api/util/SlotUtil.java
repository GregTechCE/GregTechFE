package gregtech.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class SlotUtil {

    public static void slotClickPhantom(Slot slot, int mouseButton, SlotActionType clickTypeIn, ItemStack stackHeld) {
        ItemStack stackSlot = slot.getStack();

        if (mouseButton == 2) {
            fillPhantomSlot(slot, ItemStack.EMPTY, mouseButton);
        } else if (mouseButton == 0 || mouseButton == 1) {

            if (stackSlot.isEmpty()) {
                if (!stackHeld.isEmpty() && slot.canInsert(stackHeld)) {
                    fillPhantomSlot(slot, stackHeld, mouseButton);
                }
            } else if (stackHeld.isEmpty()) {
                adjustPhantomSlot(slot, mouseButton, clickTypeIn);
            } else if (slot.canInsert(stackHeld)) {
                if (areItemsEqual(stackSlot, stackHeld)) {
                    adjustPhantomSlot(slot, mouseButton, clickTypeIn);
                } else {
                    fillPhantomSlot(slot, stackHeld, mouseButton);
                }
            }
        } else if (mouseButton == 5) {
            if (!slot.hasStack()) {
                fillPhantomSlot(slot, stackHeld, mouseButton);
            }
        }
    }

    private static void adjustPhantomSlot(Slot slot, int mouseButton, SlotActionType clickTypeIn) {
        ItemStack stackSlot = slot.getStack();
        int stackSize;
        if (clickTypeIn == SlotActionType.QUICK_MOVE) {
            stackSize = mouseButton == 0 ? (stackSlot.getCount() + 1) / 2 : stackSlot.getCount() * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.getCount() - 1 : stackSlot.getCount() + 1;
        }

        if (stackSize > slot.getMaxItemCount()) {
            stackSize = slot.getMaxItemCount();
        }

        stackSlot.setCount(stackSize);
        slot.setStack(stackSlot);
    }

    private static void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton) {
        if (stackHeld.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
            return;
        }

        int stackSize = mouseButton == 0 ? stackHeld.getCount() : 1;
        if (stackSize > slot.getMaxItemCount()) {
            stackSize = slot.getMaxItemCount();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.setCount(stackSize);
        slot.setStack(phantomStack);
    }

    public static boolean areItemsEqual(ItemStack itemStack1, ItemStack itemStack2) {
        return !ItemStack.areItemsEqual(itemStack1, itemStack2) ||
            !ItemStack.areTagsEqual(itemStack1, itemStack2);
    }
}
