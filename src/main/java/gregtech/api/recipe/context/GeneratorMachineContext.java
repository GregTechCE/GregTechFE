package gregtech.api.recipe.context;

import gregtech.api.recipe.instance.GeneratorRecipeInstance;

public interface GeneratorMachineContext<I extends GeneratorRecipeInstance> extends RecipeContext<I> {

    long getMaxVoltage();
}
