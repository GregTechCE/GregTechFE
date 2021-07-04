package gregtech.api.recipes;

import gregtech.api.recipes.context.RecipeContext;
import net.minecraft.util.Identifier;

public interface MachineRecipe<C extends RecipeContext> {

    Identifier getId();

    RecipeSerializer<?> getSerializer();

    boolean matches(C context);

    boolean canFitOutputs(C context);

    default void onStarted(C context) {
    }

    default boolean onRecipeTick(C context) {
        return true;
    }

    void addOutputs(C context);

    default void onEnded(C context) {
    }
}
