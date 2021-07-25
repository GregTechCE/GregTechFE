package gregtech.api.recipe;

import gregtech.api.recipe.context.RecipeContext;
import gregtech.api.recipe.instance.RecipeInstance;
import net.minecraft.util.Identifier;

public interface MachineRecipe<C extends RecipeContext<I>, I extends RecipeInstance> {

    Identifier getId();

    RecipeSerializer<?> getSerializer();

    Class<? extends RecipeContext<?>> getMinimumSupportedContextClass();

    boolean matches(C context);

    boolean canFitOutputs(C context, I instance);

    I startRecipe(C context);

    default void onRecipeTick(C context, I instance) {
    }

    void addOutputs(C context, I instance);

    default void onRecipeEnded(C context, I instance) {
    }
}
