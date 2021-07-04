package gregtech.api.recipes;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FixedFluidInvView;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.FixedItemInvView;

public interface MachineRecipe<C extends RecipeContext> {

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
