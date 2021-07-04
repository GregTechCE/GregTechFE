package gregtech.api.recipes.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import net.minecraft.util.JsonHelper;

public class CountableIngredient {

    private final Ingredient ingredient;
    private final int amount;

    private CountableIngredient(Ingredient ingredient, int amount) {
        Preconditions.checkNotNull(ingredient, "ingredient");
        Preconditions.checkArgument(amount > 0, "amount is less than or zero");
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public static CountableIngredient from(Ingredient ingredient) {
        return new CountableIngredient(ingredient, 1);
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

    public static CountableIngredient fromPacket(PacketByteBuf buf) {
        Ingredient ingredient = Ingredient.fromPacket(buf);
        int amount = buf.readVarInt();
        return new CountableIngredient(ingredient, amount);
    }

    public static CountableIngredient fromJson(JsonObject jsonObject) {
        JsonElement ingredientData = jsonObject.get("ingredient");
        int amount = JsonHelper.getInt(jsonObject, "amount", 1);

        Ingredient ingredient = Ingredient.fromJson(ingredientData);
        return new CountableIngredient(ingredient, amount);
    }

    public void toPacket(PacketByteBuf buf) {
        this.ingredient.write(buf);
        buf.writeVarInt(this.amount);
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("ingredient", this.ingredient.toJson());
        jsonObject.addProperty("amount", this.amount);

        return jsonObject;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getAmount() {
        return amount;
    }
}
