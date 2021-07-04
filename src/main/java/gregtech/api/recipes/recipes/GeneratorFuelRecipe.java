package gregtech.api.recipes.recipes;

import alexiil.mc.lib.attributes.fluid.FixedFluidInvView;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.collect.Lists;
import gregtech.api.recipes.CacheableMachineRecipe;
import gregtech.api.recipes.MachineRecipe;
import gregtech.api.recipes.RecipeUtil;
import gregtech.api.recipes.context.GeneratorMachineContext;
import net.minecraft.item.Item;

import java.util.Collections;
import java.util.List;

public class GeneratorFuelRecipe<C extends GeneratorMachineContext> implements MachineRecipe<C>, CacheableMachineRecipe {

    protected final FluidVolume fuelFluid;
    protected final int duration;
    protected final int EUt;

    public GeneratorFuelRecipe(FluidVolume fuelFluid, int duration, int EUt) {
        this.fuelFluid = fuelFluid;
        this.duration = duration;
        this.EUt = EUt;
    }

    private FluidAmount getFuelInInventory(FixedFluidInvView fluidInvView) {
        FluidAmount fuelStored = FluidAmount.ZERO;

        for (int i = 0; i < fluidInvView.getTankCount(); i++) {
            FluidVolume fluidVolume = fluidInvView.getInvFluid(i);
            if (fluidVolume.canMerge(this.fuelFluid)) {
                fuelStored = fuelStored.add(fluidVolume.getAmount_F());
            }
        }
        return fuelStored;
    }

    @Override
    public boolean matches(C context) {
        if (context.getMaxVoltage() < this.EUt) {
            return false;
        }

        FixedFluidInvView fluidInvView = context.getFluidInventory();
        FluidAmount fuelStored = getFuelInInventory(fluidInvView);

        return fuelStored.isGreaterThan(this.fuelFluid.getAmount_F());
    }

    @Override
    public void onStarted(C context) {
        FixedFluidInvView fluidInvView = context.getFluidInventory();
        FluidAmount fuelStored = getFuelInInventory(fluidInvView);

        int maxMultiplier = (int) Math.floor(fuelStored.div(this.fuelFluid.getAmount_F()).asInexactDouble());
        int recipeMultiplier = (int) (context.getMaxVoltage() / this.EUt);

        int actualMultiplier = Math.min(maxMultiplier, recipeMultiplier);

        FluidVolume consumedFuel = this.fuelFluid.withAmount(fuelStored);

        RecipeUtil.consumeFluids(context.getFluidInventory(), Collections.singletonList(consumedFuel));
        context.setGeneratedEUt(this.EUt * actualMultiplier);
        context.setRemainingRecipeDuration(this.duration);
    }

    @Override
    public boolean canBeCached() {
        return true;
    }

    @Override
    public List<Item> getReferencedItems() {
        return Collections.emptyList();
    }

    @Override
    public List<FluidKey> getReferencedFluids() {
        return Collections.singletonList(this.fuelFluid.getFluidKey());
    }

    @Override
    public boolean canFitOutputs(C context) {
        return true;
    }

    @Override
    public void addOutputs(C context) {
    }
}
