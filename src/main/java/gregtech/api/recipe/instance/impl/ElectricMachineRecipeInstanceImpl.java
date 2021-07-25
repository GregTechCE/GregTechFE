package gregtech.api.recipe.instance.impl;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.context.ElectricMachineContext;
import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;
import net.minecraft.nbt.NbtCompound;

public class ElectricMachineRecipeInstanceImpl<C extends ElectricMachineContext<I>, I extends ElectricMachineRecipeInstance> extends RecipeInstanceImpl<C, I> implements ElectricMachineRecipeInstance {

    private final EnergyContainer energyContainer;
    private int recipeEUt;
    private boolean notEnoughEnergy;

    public ElectricMachineRecipeInstanceImpl(RecipeMap recipeMap, C context, EnergyContainer energyContainer) {
        super(recipeMap, context);
        this.energyContainer = energyContainer;
    }

    public boolean hasNotEnoughEnergy() {
        return this.notEnoughEnergy;
    }

    @Override
    public int getRecipeEUt() {
        return this.recipeEUt;
    }

    @Override
    public void setRecipeEUt(int recipeEUt) {
        this.recipeEUt = recipeEUt;
    }

    @Override
    protected boolean tickRecipeInternal() {
        long energyRemoved = this.energyContainer.removeEnergy(this.recipeEUt, Simulation.ACTION);

        if (energyRemoved >= this.recipeEUt) {
            incrementRecipeProgress(1);
            return true;
        } else {
            this.notEnoughEnergy = true;
            incrementRecipeProgress(-2);
            return false;
        }
    }

    @Override
    public void fromTag(NbtCompound tag) {
        super.fromTag(tag);
        this.recipeEUt = tag.getInt("RecipeEUt");
        this.notEnoughEnergy = tag.getBoolean("NotEnoughEnergy");
    }

    @Override
    public NbtCompound toTag(NbtCompound nbt) {
        super.toTag(nbt);
        nbt.putInt("RecipeEUt", this.recipeEUt);
        nbt.putBoolean("NotEnoughEnergy", this.notEnoughEnergy);
        return nbt;
    }
}
