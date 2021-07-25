package gregtech.api.item.util;

import net.minecraft.item.ItemStack;

public interface ExtendedRemainderItem {

    default boolean canUseInRecipe(ItemStack stack) {
        return true;
    }

    ItemStack getRecipeRemainder(ItemStack stack);
}
