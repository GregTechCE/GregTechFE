package gregtech.api.recipes.context;

public interface ElectricMachineContext extends RecipeContext {

    long getMaxVoltage();

    void setRecipeEUt(int recipeEUt);
}
