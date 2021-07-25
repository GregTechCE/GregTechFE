package gregtech.api.recipe.context;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import gregtech.api.recipe.MachineRecipe;
import gregtech.api.recipe.instance.RecipeInstance;
import net.minecraft.nbt.NbtCompound;

import java.util.Random;

public interface RecipeContext<I extends RecipeInstance> {

    I createBlankRecipeInstance();

    default I createRecipeInstance(MachineRecipe<?, I> recipe) {
        I recipeInstance = createBlankRecipeInstance();
        recipeInstance.setRecipeChecked(recipe);
        return recipeInstance;
    }

    default I loadRecipeInstance(NbtCompound nbt) {
        I recipeInstance = createBlankRecipeInstance();
        recipeInstance.fromTag(nbt);
        return recipeInstance;
    }

    Random getRandom();
    int getTierForBoosting();

    FixedItemInv getItemInventory();
    FixedFluidInv getFluidInventory();

    FixedItemInv getOutputItemInventory();
    FixedFluidInv getOutputFluidInventory();
}
