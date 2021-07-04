package gregtech.api.recipes.crafting;

import gregtech.api.items.util.ExtendedRemainderItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class GTShapedRecipe extends ShapedRecipe {

    private final boolean transferMaxCharge;

    public GTShapedRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> input, ItemStack output, boolean transferMaxCharge) {
        super(id, group, width, height, input, output);
        this.transferMaxCharge = transferMaxCharge;
    }

    public boolean isTransferMaxCharge() {
        return transferMaxCharge;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.SHAPED;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        return CraftingRecipeUtil.processElectricItemOnOutput(inventory, getOutput(), transferMaxCharge);
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory) {
        return CraftingRecipeUtil.getRecipeRemainder(inventory);
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        for(int offsetX = 0; offsetX <= craftingInventory.getWidth() - getWidth(); ++offsetX) {
            for(int offsetY = 0; offsetY <= craftingInventory.getHeight() - getHeight(); ++offsetY) {

                if (matchesPattern(craftingInventory, offsetX, offsetY, true)) {
                    return true;
                }
                if (matchesPattern(craftingInventory, offsetX, offsetY, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesPattern(CraftingInventory inv, int offsetX, int offsetY, boolean flipped) {
        for(int x = 0; x < inv.getWidth(); ++x) {
            for(int y = 0; y < inv.getHeight(); ++y) {
                int resultX = x - offsetX;
                int resultY = y - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;

                if (resultX >= 0 && resultY >= 0 && resultX < getWidth() && resultY < getHeight()) {
                    if (flipped) {
                        ingredient = getIngredients().get(getWidth() - resultX - 1 + resultY * getWidth());
                    } else {
                        ingredient = getIngredients().get(resultX + resultY * getWidth());
                    }
                }
                ItemStack matchedStack = inv.getStack(x + y * inv.getWidth());

                if (matchedStack.getItem() instanceof ExtendedRemainderItem item) {
                    if (!item.canUseInRecipe(matchedStack)) {
                        return false;
                    }
                }
                if (!ingredient.test(matchedStack)) {
                    return false;
                }
            }
        }
        return true;
    }
}
