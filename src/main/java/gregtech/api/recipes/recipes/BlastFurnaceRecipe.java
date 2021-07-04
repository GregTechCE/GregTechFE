package gregtech.api.recipes.recipes;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import gregtech.api.recipes.ChanceEntry;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.context.EBFMachineContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class BlastFurnaceRecipe<C extends EBFMachineContext> extends ElectricMachineRecipe<C> {

    protected final int requiredTemperature;

    public BlastFurnaceRecipe(List<CountableIngredient> inputs, List<FluidVolume> fluidInputs,
                              DefaultedList<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
                              List<FluidVolume> fluidOutputs,
                              int duration, int EUt, int requiredTemperature) {
        super(inputs, fluidInputs, outputs, chancedOutputs, fluidOutputs, duration, EUt);
        this.requiredTemperature = requiredTemperature;
    }

    @Override
    public boolean matches(C context) {
        if (context.getMaxTemperature() < this.requiredTemperature) {
            return false;
        }
        return super.matches(context);
    }
}
