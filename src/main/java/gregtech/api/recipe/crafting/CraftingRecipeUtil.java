package gregtech.api.recipe.crafting;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.Ref;
import alexiil.mc.lib.attributes.misc.Reference;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.item.DischargeMode;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.item.TransferLimit;
import gregtech.api.item.util.ExtendedRemainderItem;
import gregtech.api.util.ElectricItemUtil;
import gregtech.api.util.ref.InventorySlotRef;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class CraftingRecipeUtil {

    public static DefaultedList<ItemStack> getRecipeRemainder(CraftingInventory inventory) {
        DefaultedList<ItemStack> reminderList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

        for(int i = 0; i < reminderList.size(); ++i) {
            ItemStack itemStack = inventory.getStack(i);
            if (itemStack.getItem() instanceof ExtendedRemainderItem extendedRemainderItem) {
                reminderList.set(i, extendedRemainderItem.getRecipeRemainder(itemStack));
            } else if (itemStack.getItem().hasRecipeRemainder()) {
                reminderList.set(i, new ItemStack(itemStack.getItem().getRecipeRemainder()));
            }
        }

        return reminderList;
    }

    public static ItemStack processElectricItemOnOutput(CraftingInventory craftingInventory, ItemStack outputStack, boolean transferMaxCharge) {
        Ref<ItemStack> result = new Ref<>(outputStack.copy());

        ElectricItem resultElectricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(result);
        if (resultElectricItem == null) {
            return result.get();
        }

        long newMaxCharge = resultElectricItem.getMaxCharge();

        for (int i = 0; i < craftingInventory.size(); i++) {
            Reference<ItemStack> ingredientRef = InventorySlotRef.of(craftingInventory, i);
            ElectricItem ingredientElectricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(ingredientRef);

            if (ingredientElectricItem != null && ingredientElectricItem.canProvideChargeExternally()) {
                newMaxCharge += ingredientElectricItem.getMaxCharge();

                if (transferMaxCharge) {
                    resultElectricItem.setMaxChargeOverride(newMaxCharge, Simulation.ACTION);
                }
                ElectricItemUtil.chargeElectricItem(ingredientElectricItem, resultElectricItem, DischargeMode.EXTERNAL, TransferLimit.IGNORE);
            }
        }
        return result.get();
    }
}
