package gregtech.api.recipes.recipes;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import gregtech.api.recipes.BasicMachineRecipe;
import gregtech.api.recipes.ChanceEntry;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.context.ElectricMachineContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class ElectricMachineRecipe<C extends ElectricMachineContext> extends BasicMachineRecipe<C> {

    protected final int EUt;

    public ElectricMachineRecipe(List<CountableIngredient> inputs, List<FluidVolume> fluidInputs,
                                 DefaultedList<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
                                 List<FluidVolume> fluidOutputs,
                                 int duration, int EUt) {
        super(inputs, fluidInputs, outputs, chancedOutputs, fluidOutputs, duration);
        this.EUt = EUt;
    }

    @Override
    public boolean matches(C context) {
        if (context.getMaxVoltage() < this.EUt) {
            return false;
        }
        return super.matches(context);
    }

    @Override
    public void onStarted(C context) {
        super.onStarted(context);
        context.setRecipeEUt(this.EUt);
    }
}
