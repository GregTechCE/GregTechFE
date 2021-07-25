package gregtech.api.recipe.instance.impl;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.context.GeneratorMachineContext;
import gregtech.api.recipe.instance.GeneratorRecipeInstance;
import net.minecraft.nbt.NbtCompound;

public class GeneratorRecipeInstanceImpl<C extends GeneratorMachineContext<I>, I extends GeneratorRecipeInstance> extends RecipeInstanceImpl<C, I> implements GeneratorRecipeInstance {

    private final EnergyContainer energyContainer;
    private final boolean voidExcessiveEnergy;
    private int generatedEUt;

    public GeneratorRecipeInstanceImpl(RecipeMap recipeMap, C context, EnergyContainer energyContainer, boolean voidExcessiveEnergy) {
        super(recipeMap, context);
        this.energyContainer = energyContainer;
        this.voidExcessiveEnergy = voidExcessiveEnergy;
    }

    @Override
    public int getGeneratedEUt() {
        return this.generatedEUt;
    }

    @Override
    public void setGeneratedEUt(int EUt) {
        this.generatedEUt = EUt;
    }

    @Override
    protected boolean tickRecipeInternal() {
        long energyAdded = this.energyContainer.addEnergy(this.generatedEUt, Simulation.SIMULATE);

        if (energyAdded == this.generatedEUt || this.voidExcessiveEnergy) {
            this.energyContainer.addEnergy(this.generatedEUt, Simulation.ACTION);
            incrementRecipeProgress(1);
            return true;
        }
        return false;
    }

    @Override
    public void fromTag(NbtCompound tag) {
        super.fromTag(tag);
        this.generatedEUt = tag.getInt("GeneratedEUt");
    }

    @Override
    public NbtCompound toTag(NbtCompound nbt) {
        super.toTag(nbt);
        nbt.putInt("GeneratedEUt", this.generatedEUt);
        return nbt;
    }
}
