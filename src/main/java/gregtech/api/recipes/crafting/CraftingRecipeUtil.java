package gregtech.api.recipes.crafting;

import alexiil.mc.lib.attributes.misc.Reference;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.items.util.ExtendedRemainderItem;
import gregtech.api.util.ElectricItemUtil;
import gregtech.api.util.InventorySlotReference;
import gregtech.api.util.SimpleReference;
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
        Reference<ItemStack> result = new SimpleReference<>(outputStack.copy());

        ElectricItem resultElectricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(result);
        if (resultElectricItem == null) {
            return result.get();
        }

        long newMaxCharge = resultElectricItem.getMaxCharge();

        for (int i = 0; i < craftingInventory.size(); i++) {
            Reference<ItemStack> ingredientRef = new InventorySlotReference(craftingInventory, i);
            ElectricItem ingredientElectricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(ingredientRef);

            if (ingredientElectricItem != null && ingredientElectricItem.canProvideChargeExternally()) {
                newMaxCharge += ingredientElectricItem.getMaxCharge();

                if (transferMaxCharge) {
                    resultElectricItem.setMaxChargeOverride(newMaxCharge);
                }
                ElectricItemUtil.chargeElectricItem(ingredientElectricItem, resultElectricItem, true, true);
            }
        }
        return result.get();
    }
}
