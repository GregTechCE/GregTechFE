package gregtech.api.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class GTShapelessRecipeSerializer implements RecipeSerializer<GTShapelessRecipe> {

    @Override
    public GTShapelessRecipe read(Identifier id, JsonObject jsonObject) {
        String group = JsonHelper.getString(jsonObject, "group", "");
        boolean transferMaxCharge = JsonHelper.getBoolean(jsonObject, "transfer_max_charge", false);

        DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
        if (ingredients.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (ingredients.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        }

        ItemStack outputStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
        return new GTShapelessRecipe(id, group, outputStack, ingredients, transferMaxCharge);
    }

    @Override
    public GTShapelessRecipe read(Identifier id, PacketByteBuf buf) {
        String group = buf.readString();
        boolean transferMaxCharge = buf.readBoolean();

        int ingredientsAmount = buf.readVarInt();
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(ingredientsAmount, Ingredient.EMPTY);

        for(int i = 0; i < ingredients.size(); i++) {
            ingredients.set(i, Ingredient.fromPacket(buf));
        }

        ItemStack outputStack = buf.readItemStack();
        return new GTShapelessRecipe(id, group, outputStack, ingredients, transferMaxCharge);
    }

    @Override
    public void write(PacketByteBuf buf, GTShapelessRecipe recipe) {
        buf.writeString(recipe.getGroup());
        buf.writeBoolean(recipe.isTransferMaxCharge());

        buf.writeVarInt(recipe.getIngredients().size());
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.write(buf);
        }

        buf.writeItemStack(recipe.getOutput());
    }

    private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();

        for(int i = 0; i < json.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(json.get(i));
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }
}
