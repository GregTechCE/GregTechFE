package gregtech.api.recipe.context;

import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;

public interface EBFMachineContext<I extends ElectricMachineRecipeInstance> extends ElectricMachineContext<I> {

    int getMaxTemperature();
}
