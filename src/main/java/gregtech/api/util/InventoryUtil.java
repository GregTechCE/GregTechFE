package gregtech.api.util;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {

    public static boolean insertFluidIntoInventory(FixedFluidInv fluidInv, List<FluidVolume> stacks, Simulation simulation) {
        ArrayList<FluidVolume> remainingStacks = new ArrayList<>(stacks);

        for (int slotIndex = 0; slotIndex < fluidInv.getTankCount(); slotIndex++) {
            FluidVolume inventoryFluid = fluidInv.getInvFluid(slotIndex);
            FluidAmount tankCapacity = fluidInv.getMaxAmount_F(slotIndex);

            if (!inventoryFluid.isEmpty() && inventoryFluid.getAmount_F().isLessThan(tankCapacity)) {
                insertFluidSingle(fluidInv, slotIndex, remainingStacks, simulation);

                if (remainingStacks.isEmpty()) {
                    return true;
                }
            }
        }

        for (int slotIndex = 0; slotIndex < fluidInv.getTankCount(); slotIndex++) {
            FluidVolume inventoryFluid = fluidInv.getInvFluid(slotIndex);

            if (inventoryFluid.isEmpty()) {
                insertFluidSingle(fluidInv, slotIndex, remainingStacks, simulation);

                if (remainingStacks.isEmpty()) {
                    return true;
                }
            }
        }
        return remainingStacks.isEmpty();
    }

    public static boolean insertItemsIntoInventory(FixedItemInv itemInv, List<ItemStack> stacks, Simulation simulation) {
        ArrayList<ItemStack> remainingStacks = new ArrayList<>(stacks);

        //First, try to condense stacks in existing slots
        for (int slotIndex = 0; slotIndex < itemInv.getSlotCount(); slotIndex++) {
            ItemStack stackInSlot = itemInv.getInvStack(slotIndex);
            int maxStackSize = itemInv.getMaxAmount(slotIndex, stackInSlot);

            if (!stackInSlot.isEmpty() && maxStackSize > stackInSlot.getCount()) {
                insertItemSingle(itemInv, slotIndex, remainingStacks, simulation);

                if (remainingStacks.isEmpty()) {
                    return true;
                }
            }
        }

        //Now, try to insert the excess into the empty slots in the inventory
        for (int slotIndex = 0; slotIndex < itemInv.getSlotCount(); slotIndex++) {
            ItemStack stackInSlot = itemInv.getInvStack(slotIndex);

            //Skip all slots with existing items, we have already inserted items into partially full slots
            if (stackInSlot.isEmpty()) {
                insertItemSingle(itemInv, slotIndex, remainingStacks, simulation);

                if (remainingStacks.isEmpty()) {
                    return true;
                }
            }
        }
        return remainingStacks.isEmpty();
    }

    private static void insertFluidSingle(FixedFluidInv fluidInv, int slotIndex, List<FluidVolume> remainingStacks, Simulation simulation) {
        for (int i = 0; i < remainingStacks.size(); i++) {
            FluidVolume remainingStack = remainingStacks.get(i);
            FluidVolume excessStack = fluidInv.insertFluid(slotIndex, remainingStack, simulation);

            if (!excessStack.getAmount_F().equals(remainingStack.getAmount_F())) {
                remainingStacks.set(i, excessStack);

                if (excessStack.isEmpty()) {
                    remainingStacks.remove(i);
                }
                return;
            }
        }
    }

    private static void insertItemSingle(FixedItemInv itemInv, int slotIndex, List<ItemStack> remainingStacks, Simulation simulation) {
        for (int i = 0; i < remainingStacks.size(); i++) {
            ItemStack remainingStack = remainingStacks.get(i);
            ItemStack excessStack = itemInv.insertStack(slotIndex, remainingStack, simulation);

            if (excessStack.getCount() != remainingStack.getCount()) {
                remainingStacks.set(i, excessStack);

                if (excessStack.isEmpty()) {
                    remainingStacks.remove(i);
                }
                return;
            }
        }
    }

    public static List<ItemStack> compactItemStacks(List<ItemStack> stacks) {
        ArrayList<ItemStack> compactedStacks = new ArrayList<>();

        mainLoop: for (ItemStack rawStack : stacks) {
            for (ItemStack compactedStack : compactedStacks) {
                if (ItemStack.canCombine(compactedStack, rawStack)) {
                    compactedStack.increment(rawStack.getCount());
                    continue mainLoop;
                }
            }
            compactedStacks.add(rawStack.copy());
        }
        return compactedStacks;
    }
}
