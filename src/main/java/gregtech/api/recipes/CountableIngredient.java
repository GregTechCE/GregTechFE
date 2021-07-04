package gregtech.api.recipes;

import com.google.common.base.Preconditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;

public class CountableIngredient {

    private final Ingredient ingredient;
    private final int amount;

    private CountableIngredient(Ingredient ingredient, int amount) {
        Preconditions.checkNotNull(ingredient, "ingredient");
        Preconditions.checkArgument(amount > 0, "amount is less than or zero");
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public static CountableIngredient from(Ingredient ingredient, int amount) {
        return new CountableIngredient(ingredient, amount);
    }

    public static CountableIngredient from(ItemStack stack, int amount) {
        return new CountableIngredient(Ingredient.ofStacks(stack), amount);
    }

    public static CountableIngredient from(Tag.Identified<Item> tag, int count) {
        return new CountableIngredient(Ingredient.fromTag(tag), count);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getAmount() {
        return amount;
    }
}
