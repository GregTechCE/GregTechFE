package gregtech.api.recipe.context;

import gregtech.api.recipe.instance.RecipeInstance;

public interface PBFMachineContext<I extends RecipeInstance> extends RecipeContext<I> {

    int getStoredFuelUnits();

    void consumeFuel(int fuelUnits);
}
