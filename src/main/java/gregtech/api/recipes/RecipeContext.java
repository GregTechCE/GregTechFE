package gregtech.api.recipes;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.item.FixedItemInv;

import java.util.Random;

public interface RecipeContext {

    Random getRandom();
    int getTierForBoosting();

    void setRemainingRecipeDuration(int recipeDuration);

    FixedItemInv getItemInventory();
    FixedFluidInv getFluidInventory();


    FixedItemInv getOutputItemInventory();
    FixedFluidInv getOutputFluidInventory();
}
