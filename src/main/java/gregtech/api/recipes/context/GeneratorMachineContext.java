package gregtech.api.recipes.context;

import gregtech.api.recipes.RecipeContext;

public interface GeneratorMachineContext extends RecipeContext {

    long getMaxVoltage();

    void setGeneratedEUt(int EUt);
}
