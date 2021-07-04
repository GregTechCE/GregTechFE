package gregtech.api.recipes.context;

import gregtech.api.recipes.RecipeContext;

public interface ElectricMachineContext extends RecipeContext {

    long getMaxVoltage();

    void setRecipeEUt(int recipeEUt);
}
