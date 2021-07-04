package gregtech.api.recipes;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import gregtech.api.util.InventoryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BasicMachineRecipe<C extends RecipeContext> implements MachineRecipe<C>, CacheableMachineRecipe {

    protected final int duration;
    protected final List<CountableIngredient> inputs;
    protected final List<FluidVolume> fluidInputs;

    protected final DefaultedList<ItemStack> outputs;
    protected final List<ChanceEntry> chancedOutputs;
    protected final List<FluidVolume> fluidOutputs;

    protected final List<ItemStack> combinedOutputs;

    public BasicMachineRecipe(List<CountableIngredient> inputs, List<FluidVolume> fluidInputs,
                              DefaultedList<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
                              List<FluidVolume> fluidOutputs,
                              int duration) {
        this.duration = duration;
        this.inputs = inputs;
        this.fluidInputs = fluidInputs;
        this.outputs = outputs;
        this.chancedOutputs = chancedOutputs;
        this.fluidOutputs = fluidOutputs;

        ArrayList<ItemStack> combinedOutputs = new ArrayList<>(outputs);
        for (ChanceEntry chanceEntry : chancedOutputs) {
            combinedOutputs.add(chanceEntry.getItemStack());
        }
        this.combinedOutputs = InventoryUtil.compactItemStacks(combinedOutputs);
    }

    @Override
    public boolean matches(C context) {
        if (!RecipeUtil.matchesItems(context.getItemInventory(), inputs)) {
            return false;
        }
        return RecipeUtil.matchesFluids(context.getFluidInventory(), fluidInputs);
    }

    @Override
    public void onStarted(C context) {
        RecipeUtil.consumeItems(context.getItemInventory(), this.inputs);
        RecipeUtil.consumeFluids(context.getFluidInventory(), this.fluidInputs);
        context.setRemainingRecipeDuration(this.duration);
    }

    @Override
    public boolean canFitOutputs(C context) {
        if (!InventoryUtil.insertItemsIntoInventory(context.getOutputItemInventory(), combinedOutputs, Simulation.SIMULATE)) {
            return false;
        }
        return InventoryUtil.insertFluidIntoInventory(context.getOutputFluidInventory(), fluidOutputs, Simulation.SIMULATE);
    }

    @Override
    public void addOutputs(C context) {
        ArrayList<ItemStack> resultOutputs = new ArrayList<>(outputs);
        for (ChanceEntry chanceEntry : chancedOutputs) {
            if (chanceEntry.rollChanceEntry(context)) {
                resultOutputs.add(chanceEntry.getItemStack());
            }
        }

        InventoryUtil.insertItemsIntoInventory(context.getOutputItemInventory(),
                InventoryUtil.compactItemStacks(resultOutputs), Simulation.ACTION);
        InventoryUtil.insertFluidIntoInventory(context.getOutputFluidInventory(),
                fluidOutputs, Simulation.ACTION);
    }

    @Override
    public List<Item> getReferencedItems() {
        return this.inputs.stream()
                .map(CountableIngredient::getIngredient)
                .flatMap(ingredient -> Arrays.stream(ingredient.getMatchingStacksClient()))
                .map(ItemStack::getItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<FluidKey> getReferencedFluids() {
        return this.fluidInputs.stream()
                .map(FluidVolume::getFluidKey)
                .collect(Collectors.toList());
    }

    @Override
    public boolean canBeCached() {
        return true;
    }
}
