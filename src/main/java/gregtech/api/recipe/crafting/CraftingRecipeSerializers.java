package gregtech.api.recipe.crafting;

import gregtech.api.GTValues;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CraftingRecipeSerializers {

    public static final RecipeSerializer<GTShapedRecipe> SHAPED;
    public static final RecipeSerializer<GTShapelessRecipe> SHAPELESS;

    private static <T extends Recipe<?>> RecipeSerializer<T> register(String name, RecipeSerializer<T> serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(GTValues.MODID, name), serializer);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void register() {
        SHAPED.getClass();
    }

    static {
        SHAPED = register("shaped", new GTShapedRecipeSerializer());
        SHAPELESS = register("shapeless", new GTShapelessRecipeSerializer());
    }
}
