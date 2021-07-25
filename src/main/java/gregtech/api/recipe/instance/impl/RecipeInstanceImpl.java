package gregtech.api.recipe.instance.impl;

import com.google.common.base.Preconditions;
import gregtech.api.recipe.MachineRecipe;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.context.RecipeContext;
import gregtech.api.recipe.instance.RecipeInstance;
import gregtech.api.recipe.util.RecipeDataType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RecipeInstanceImpl<C extends RecipeContext<I>, I extends RecipeInstance> implements RecipeInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeInstanceImpl.class);
    private final RecipeMap recipeMap;
    private final C context;
    private MachineRecipe<C, I> recipe;
    private final Map<RecipeDataType<?>, Object> recipeData = new HashMap<>();
    private int duration;
    private int recipeProgress;
    private boolean recipeOutputsJammed;
    private boolean recipeFinished;

    public RecipeInstanceImpl(RecipeMap recipeMap, C context) {
        this.recipeMap = recipeMap;
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    protected I getRecipeInstance() {
        return (I) this;
    }

    @Override
    public boolean isValid() {
        return this.recipe != null;
    }

    @Override
    public int getRecipeDuration() {
        return this.duration;
    }

    @Override
    public void setRecipeDuration(int recipeDuration) {
        this.duration = recipeDuration;
    }

    @Override
    public int getRecipeProgress() {
        return this.recipeProgress;
    }

    @Override
    public void incrementRecipeProgress(int delta) {
        this.recipeProgress = MathHelper.clamp(this.recipeProgress + delta, 0, this.duration);
    }

    @Override
    public <T> void setRecipeData(RecipeDataType<T> type, T data) {
        this.recipeData.put(type, data);
    }

    @Override
    public <T> Optional<T> getRecipeData(RecipeDataType<T> type) {
        Object existingData = this.recipeData.get(type);
        if (existingData == null) {
            return Optional.empty();
        }
        return Optional.of(type.castRecipeData(existingData));
    }

    @Override
    public void recheckRecipeOutputs() {
        this.recipeOutputsJammed = false;
    }

    @Override
    public boolean hasRecipeFinished() {
        return this.recipeFinished;
    }

    protected boolean tickRecipeInternal() {
        incrementRecipeProgress(1);
        return true;
    }

    @Override
    public void tickRecipe() {
        if (this.recipe == null) {
            this.recipeFinished = true;
            return;
        }
        if (this.duration > this.recipeProgress) {
            if (tickRecipeInternal()) {
                this.recipe.onRecipeTick(context, getRecipeInstance());
            }
        }
        if (this.recipeProgress >= this.duration) {
            if (canFinishRecipe()) {
                onRecipeFinished();
            }
        }
    }

    protected boolean canFinishRecipe() {
        if (this.recipeOutputsJammed) {
            return false;
        }

        boolean canFitOutputs = this.recipe.canFitOutputs(context, getRecipeInstance());
        this.recipeOutputsJammed = !canFitOutputs;
        return canFitOutputs;
    }

    protected void onRecipeFinished() {
        this.recipe.addOutputs(context, getRecipeInstance());
        this.recipe.onRecipeEnded(context, getRecipeInstance());
        this.recipeFinished = true;
    }

    private static <T> NbtCompound serializeRecipeData(RecipeDataType<T> dataType, Object data) {
        return dataType.serializeRecipeData(dataType.castRecipeData(data));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setRecipeChecked(MachineRecipe<?, ?> newRecipe) {
        Preconditions.checkState(this.recipe == null, "Cannot change already set recipe object");
        Preconditions.checkArgument(newRecipe.getMinimumSupportedContextClass().isAssignableFrom(this.context.getClass()));
        this.recipe = (MachineRecipe<C, I>) newRecipe;
    }

    @Override
    public void fromTag(NbtCompound tag) {
        Identifier recipeId = Identifier.tryParse(tag.getString("Recipe"));
        if (recipeId != null) {
            MachineRecipe<?, ?> recipe = this.recipeMap.getRecipeById(recipeId);
            if (recipe != null) {
                setRecipeChecked(recipe);
            } else {
                LOGGER.error("Failed to find recipe with id {} in the recipe map {}", recipeId, recipeMap.getRecipeType());
            }
        }

        this.duration = tag.getInt("Duration");
        this.recipeProgress = tag.getInt("Progress");
        this.recipeFinished = tag.getBoolean("Finished");

        NbtCompound recipeData = tag.getCompound("RecipeData");

        for (String recipeDataTypeIdString : recipeData.getKeys()) {
            Identifier typeId = Identifier.tryParse(recipeDataTypeIdString);
            NbtElement element = recipeData.get(recipeDataTypeIdString);

            if (typeId != null && element instanceof NbtCompound data) {
                RecipeDataType<?> recipeDataType = RecipeDataType.REGISTRY.get(typeId);

                if (recipeDataType == null) {
                    LOGGER.error("Failed to find recipe data type {} referenced by recipe {}", typeId, recipeId);
                    continue;
                }
                Object deserializedData = recipeDataType.deserializeRecipeData(data);
                this.recipeData.put(recipeDataType, deserializedData);
            }
        }
    }

    @Override
    public NbtCompound toTag(NbtCompound nbt) {
        nbt.putString("Recipe", this.recipe.getId().toString());
        nbt.putInt("Duration", this.duration);
        nbt.putInt("Progress", this.recipeProgress);
        nbt.putBoolean("Finished", this.recipeFinished);

        NbtCompound recipeData = new NbtCompound();

        for (RecipeDataType<?> recipeDataType : this.recipeData.keySet()) {
            Identifier dataId = recipeDataType.getId();
            NbtCompound data = serializeRecipeData(recipeDataType, this.recipeData.get(recipeDataType));
            recipeData.put(dataId.toString(), data);
        }
        nbt.put("RecipeData", recipeData);

        return nbt;
    }
}
