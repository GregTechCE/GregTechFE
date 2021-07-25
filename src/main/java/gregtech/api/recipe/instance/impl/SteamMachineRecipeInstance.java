package gregtech.api.recipe.instance.impl;

import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.context.ElectricMachineContext;
import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;
import net.minecraft.nbt.NbtCompound;

public class SteamMachineRecipeInstance<C extends ElectricMachineContext<I>, I extends ElectricMachineRecipeInstance> extends RecipeInstanceImpl<C, I> implements ElectricMachineRecipeInstance {

    private final FluidExtractable fluidTank;
    private final FluidKey steamFluid;
    private final FluidAmount fluidPerEnergyUnit;

    private int recipeEUt;

    public SteamMachineRecipeInstance(RecipeMap recipeMap, C context, FluidExtractable fluidTank, FluidKey steamFluid, FluidAmount fluidPerEnergyUnit) {
        super(recipeMap, context);
        this.fluidTank = fluidTank;
        this.steamFluid = steamFluid;
        this.fluidPerEnergyUnit = fluidPerEnergyUnit;
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
        FluidAmount steamToExtract = this.fluidPerEnergyUnit.mul(this.recipeEUt);
        FluidVolume steamExtracted = this.fluidTank.extract(this.steamFluid, steamToExtract);

        if (steamExtracted.getAmount_F().isGreaterThanOrEqual(steamToExtract)) {
            incrementRecipeProgress(1);
            return true;
        }
        return false;
    }

    @Override
    public void fromTag(NbtCompound tag) {
        super.fromTag(tag);
        this.recipeEUt = tag.getInt("RecipeEUt");
    }

    @Override
    public NbtCompound toTag(NbtCompound nbt) {
        super.toTag(nbt);
        nbt.putInt("RecipeEUt", this.recipeEUt);
        return nbt;
    }
}
