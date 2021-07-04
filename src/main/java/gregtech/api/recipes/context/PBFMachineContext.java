package gregtech.api.recipes.context;

public interface PBFMachineContext extends RecipeContext {

    int getStoredFuelUnits();

    void consumeFuel(int fuelUnits);
}
