package gregtech.api.recipe.instance;

import alexiil.mc.lib.attributes.misc.Saveable;
import gregtech.api.recipe.MachineRecipe;
import gregtech.api.recipe.util.RecipeDataType;

import java.util.Optional;

public interface RecipeInstance extends Saveable {

    int getRecipeDuration();
    void setRecipeDuration(int recipeDuration);

    int getRecipeProgress();
    void incrementRecipeProgress(int delta);

    <T> void setRecipeData(RecipeDataType<T> type, T data);
    <T> Optional<T> getRecipeData(RecipeDataType<T> type);

    void setRecipeChecked(MachineRecipe<?, ?> newRecipe);
    boolean isValid();
    boolean hasRecipeFinished();
    void recheckRecipeOutputs();
    void tickRecipe();
}
