package gregtech.api.recipes.crafting;

import gregtech.api.items.util.ExtendedRemainderItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class GTShapelessRecipe extends ShapelessRecipe {

    private final boolean transferMaxCharge;

    public GTShapelessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, boolean transferMaxCharge) {
        super(id, group, output, input);
        this.transferMaxCharge = transferMaxCharge;
    }

    public boolean isTransferMaxCharge() {
        return transferMaxCharge;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.SHAPELESS;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        return RecipeUtil.processElectricItemOnOutput(inventory, getOutput(), transferMaxCharge);
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory) {
        return RecipeUtil.getRecipeRemainder(inventory);
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int ingredientsMatched = 0;

        for(int i = 0; i < craftingInventory.size(); i++) {
            ItemStack itemStack = craftingInventory.getStack(i);

            if (itemStack.getItem() instanceof ExtendedRemainderItem item) {
                if (!item.canUseInRecipe(itemStack)) {
                    return false;
                }
            }

            if (!itemStack.isEmpty()) {
                recipeMatcher.addInput(itemStack, 1);
                ingredientsMatched++;
            }
        }

        return ingredientsMatched == getIngredients().size() &&
                recipeMatcher.match(this, null);
    }
}
