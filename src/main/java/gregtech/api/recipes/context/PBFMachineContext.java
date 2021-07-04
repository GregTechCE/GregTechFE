package gregtech.api.recipes.context;

import gregtech.api.recipes.RecipeContext;

public interface PBFMachineContext extends RecipeContext {

    int getStoredFuelUnits();

    void consumeFuel(int fuelUnits);
}
