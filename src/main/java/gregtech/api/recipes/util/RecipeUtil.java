package gregtech.api.recipes.util;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FixedFluidInvView;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.FixedItemInvView;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class RecipeUtil {

    public static void consumeItems(FixedItemInv itemInv, List<CountableIngredient> inputs) {
        if (inputs.isEmpty()) {
            return;
        }

        int[] remainingIngredients = new int[inputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            remainingIngredients[i] = inputs.get(i).getAmount();
        }

        for (int slotIndex = itemInv.getSlotCount() - 1; slotIndex >= 0; slotIndex--) {
            for (int ingredientIndex = 0; ingredientIndex < inputs.size(); ingredientIndex++) {

                int remainingCount = remainingIngredients[ingredientIndex];
                if (remainingCount > 0) {
                    ItemStack itemStack = itemInv.getInvStack(slotIndex);
                    CountableIngredient ingredient = inputs.get(ingredientIndex);

                    if (ingredient.getIngredient().test(itemStack)) {
                        ItemStack extractStack = itemInv.extractStack(slotIndex, null, ItemStack.EMPTY, remainingCount, Simulation.ACTION);

                        if (!extractStack.isEmpty()) {
                            remainingIngredients[ingredientIndex] -= extractStack.getCount();
                        }
                    }
                }
            }
        }
    }

    public static void consumeFluids(FixedFluidInv fluidInv, List<FluidVolume> fluidInputs) {
        if (fluidInputs.isEmpty()) {
            return;
        }

        FluidAmount[] remainingFluids = new FluidAmount[fluidInputs.size()];
        for (int i = 0; i < fluidInputs.size(); i++) {
            remainingFluids[i] = fluidInputs.get(i).getAmount_F();
        }

        for (int slotIndex = 0; slotIndex < fluidInv.getTankCount(); slotIndex++) {
            for (int fluidIndex = 0; fluidIndex < fluidInputs.size(); fluidIndex++) {

                FluidAmount remainingAmount = remainingFluids[fluidIndex];
                if (remainingAmount.isPositive()) {
                    FluidVolume fluidVolume = fluidInv.getInvFluid(slotIndex);
                    FluidVolume ingredient = fluidInputs.get(fluidIndex);

                    if (ingredient.canMerge(fluidVolume)) {
                        FluidVolume extractedStack = fluidInv.extractFluid(slotIndex, null, FluidVolumeUtil.EMPTY, remainingAmount, Simulation.ACTION);

                        if (!extractedStack.isEmpty()) {
                            remainingFluids[fluidIndex] = remainingAmount.sub(extractedStack.getAmount_F());
                        }
                    }
                }
            }
        }
    }

    public static boolean matchesItems(FixedItemInvView itemInvView, List<CountableIngredient> inputs) {
        if (inputs.isEmpty()) {
            return true;
        }

        int[] collectedIngredients = new int[inputs.size()];

        for (int slotIndex = 0; slotIndex < itemInvView.getSlotCount(); slotIndex++) {
            for (int ingredientIndex = 0; ingredientIndex < inputs.size(); ingredientIndex++) {

                ItemStack itemStack = itemInvView.getInvStack(slotIndex);
                CountableIngredient ingredient = inputs.get(ingredientIndex);

                if (ingredient.getIngredient().test(itemStack)) {
                    collectedIngredients[ingredientIndex] += itemStack.getCount();
                }
            }
        }

        for (int ingredientIndex = 0; ingredientIndex < collectedIngredients.length; ingredientIndex++) {
            CountableIngredient ingredient = inputs.get(ingredientIndex);
            int ingredientAmount = collectedIngredients[ingredientIndex];

            if (ingredient.getAmount() > ingredientAmount) {
                return false;
            }
        }
        return true;
    }

    public static boolean matchesFluids(FixedFluidInvView fluidInvView, List<FluidVolume> fluidInputs) {
        if (fluidInputs.isEmpty()) {
            return true;
        }

        FluidAmount[] collectedFluids = new FluidAmount[fluidInputs.size()];
        Arrays.fill(collectedFluids, FluidAmount.ZERO);

        for (int slotIndex = 0; slotIndex < fluidInvView.getTankCount(); slotIndex++) {
            for (int fluidIndex = 0; fluidIndex < fluidInputs.size(); fluidIndex++) {

                FluidVolume fluidVolume = fluidInvView.getInvFluid(slotIndex);
                FluidVolume ingredient = fluidInputs.get(fluidIndex);

                if (ingredient.canMerge(fluidVolume)) {
                    FluidAmount amount = collectedFluids[fluidIndex];
                    collectedFluids[fluidIndex] = amount.add(fluidVolume.getAmount_F());
                }
            }
        }

        for (int fluidIndex = 0; fluidIndex < collectedFluids.length; fluidIndex++) {
            FluidVolume ingredient = fluidInputs.get(fluidIndex);
            FluidAmount fluidAmount = collectedFluids[fluidIndex];

            if (ingredient.getAmount_F().isGreaterThan(fluidAmount)) {
                return false;
            }
        }
        return true;
    }
}
