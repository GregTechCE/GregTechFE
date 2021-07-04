package gregtech.api.recipes.recipes;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import gregtech.api.recipes.BasicMachineRecipe;
import gregtech.api.recipes.ChanceEntry;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.context.PBFMachineContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class PBFRecipe<C extends PBFMachineContext> extends BasicMachineRecipe<C> {

    protected final int fuelCost;

    public PBFRecipe(List<CountableIngredient> inputs, List<FluidVolume> fluidInputs,
                     DefaultedList<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
                     List<FluidVolume> fluidOutputs,
                     int duration, int fuelCost) {
        super(inputs, fluidInputs, outputs, chancedOutputs, fluidOutputs, duration);
        this.fuelCost = fuelCost;
    }

    @Override
    public boolean matches(C context) {
        if (context.getStoredFuelUnits() < this.fuelCost) {
            return false;
        }
        return super.matches(context);
    }

    @Override
    public void onStarted(C context) {
        context.consumeFuel(this.fuelCost);
        super.onStarted(context);
    }
}
