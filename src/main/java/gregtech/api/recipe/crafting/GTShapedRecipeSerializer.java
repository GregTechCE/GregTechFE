package gregtech.api.recipe.crafting;

import com.google.gson.JsonObject;
import gregtech.mixin.accessor.ShapedRecipeAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class GTShapedRecipeSerializer implements RecipeSerializer<GTShapedRecipe> {

    @Override
    public GTShapedRecipe read(Identifier id, JsonObject jsonObject) {
        String group = JsonHelper.getString(jsonObject, "group", "");
        boolean transferMaxCharge = JsonHelper.getBoolean(jsonObject, "transfer_max_charge", false);

        Map<String, Ingredient> ingredientMap = ShapedRecipeAccessor.readSymbols(JsonHelper.getObject(jsonObject, "key"));
        String[] pattern = ShapedRecipeAccessor.removePadding(ShapedRecipeAccessor.getPattern(JsonHelper.getArray(jsonObject, "pattern")));

        int recipeWidth = pattern[0].length();
        int recipeHeight = pattern.length;
        DefaultedList<Ingredient> input = ShapedRecipeAccessor.createPatternMatrix(pattern, ingredientMap, recipeWidth, recipeHeight);
        ItemStack outputStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));

        return new GTShapedRecipe(id, group, recipeWidth, recipeHeight, input, outputStack, transferMaxCharge);
    }

    @Override
    public GTShapedRecipe read(Identifier id, PacketByteBuf buf) {
        int recipeWidth = buf.readVarInt();
        int recipeHeight = buf.readVarInt();
        String recipeGroup = buf.readString();
        boolean transferMaxCharge = buf.readBoolean();

        DefaultedList<Ingredient> input = DefaultedList.ofSize(recipeWidth * recipeHeight, Ingredient.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            input.set(i, Ingredient.fromPacket(buf));
        }

        ItemStack outputStack = buf.readItemStack();
        return new GTShapedRecipe(id, recipeGroup, recipeWidth, recipeHeight, input, outputStack, transferMaxCharge);
    }

    @Override
    public void write(PacketByteBuf buf, GTShapedRecipe recipe) {
        buf.writeVarInt(recipe.getWidth());
        buf.writeVarInt(recipe.getHeight());
        buf.writeString(recipe.getGroup());
        buf.writeBoolean(recipe.isTransferMaxCharge());

        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.write(buf);
        }

        buf.writeItemStack(recipe.getOutput());
    }
}
