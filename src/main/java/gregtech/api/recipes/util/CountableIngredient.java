package gregtech.api.recipes.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;

public class CountableIngredient {

    private final Ingredient ingredient;
    private final int amount;
    private final boolean notConsumed;

    private CountableIngredient(Ingredient ingredient, int amount, boolean notConsumed) {
        Preconditions.checkNotNull(ingredient, "ingredient");
        Preconditions.checkArgument(amount > 0, "amount must be positive");
        this.ingredient = ingredient;
        this.amount = amount;
        this.notConsumed = notConsumed;
    }

    public static CountableIngredient notConsumed(Ingredient ingredient, int amount) {
        return new CountableIngredient(ingredient, amount, true);
    }

    public static CountableIngredient from(Ingredient ingredient, int amount) {
        return new CountableIngredient(ingredient, amount, false);
    }

    public static CountableIngredient fromPacket(PacketByteBuf buf) {
        Ingredient ingredient = Ingredient.fromPacket(buf);
        int amount = buf.readVarInt();
        boolean notConsumed = buf.readBoolean();

        return new CountableIngredient(ingredient, amount, notConsumed);
    }

    public static CountableIngredient fromJson(JsonObject jsonObject) {
        JsonElement ingredientData = jsonObject.get("ingredient");
        int amount = JsonHelper.getInt(jsonObject, "amount", 1);
        boolean notConsumed = JsonHelper.getBoolean(jsonObject, "not_consumed", false);

        Ingredient ingredient = Ingredient.fromJson(ingredientData);
        return new CountableIngredient(ingredient, amount, notConsumed);
    }

    public void toPacket(PacketByteBuf buf) {
        this.ingredient.write(buf);
        buf.writeVarInt(this.amount);
        buf.writeBoolean(this.notConsumed);
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("ingredient", this.ingredient.toJson());
        if (this.amount != 1) {
            jsonObject.addProperty("amount", this.amount);
        }
        if (this.notConsumed) {
            jsonObject.addProperty("not_consumed", true);
        }

        return jsonObject;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isNotConsumed() {
        return notConsumed;
    }
}
