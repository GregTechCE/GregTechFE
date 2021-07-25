package gregtech.api.recipe.context;

import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;
import gregtech.api.recipe.util.OverclockResult;
import gregtech.api.util.VoltageTier;

public interface ElectricMachineContext<I extends ElectricMachineRecipeInstance> extends RecipeContext<I> {

    double OVERCLOCKING_FACTOR = 2.8;

    long getMaxVoltage();

    VoltageTier getOverclockingTier();

    default OverclockResult applyOverclocking(int recipeEUt, int recipeDuration) {
        VoltageTier overclockingTier = getOverclockingTier();
        if (overclockingTier == VoltageTier.ULV) {
            return new OverclockResult(recipeEUt, recipeDuration);
        }

        VoltageTier previousTier = overclockingTier.getPreviousTier();
        int resultEUt = recipeEUt;
        double resultDuration = recipeDuration;

        while (resultDuration >= OVERCLOCKING_FACTOR && resultEUt <= previousTier.getVoltage()) {
            resultEUt *= 4;
            resultDuration /= OVERCLOCKING_FACTOR;
        }
        return new OverclockResult(resultEUt, (int) Math.ceil(resultDuration));
    }
}
