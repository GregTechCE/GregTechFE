package gregtech.mixin.accessor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {

    @Invoker
    static Map<String, Ingredient> readSymbols(JsonObject json) {
        throw new IllegalStateException("ShapedRecipeAccessor not transformed");
    }

    @Invoker
    static String[] getPattern(JsonArray patternArray) {
        throw new IllegalStateException("ShapedRecipeAccessor not transformed");
    }

    @Invoker
    static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
        throw new IllegalStateException("ShapedRecipeAccessor not transformed");
    }

    @Invoker
    static String[] removePadding(String... pattern) {
        throw new IllegalStateException("ShapedRecipeAccessor not transformed");
    }
}
