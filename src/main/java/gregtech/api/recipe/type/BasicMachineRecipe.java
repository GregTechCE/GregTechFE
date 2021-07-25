package gregtech.api.recipe.type;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import gregtech.api.recipe.CacheableMachineRecipe;
import gregtech.api.recipe.MachineRecipe;
import gregtech.api.recipe.RecipeSerializer;
import gregtech.api.recipe.context.RecipeContext;
import gregtech.api.recipe.instance.RecipeInstance;
import gregtech.api.recipe.util.ChanceEntry;
import gregtech.api.recipe.util.CountableIngredient;
import gregtech.api.recipe.util.RecipeUtil;
import gregtech.api.util.InventoryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BasicMachineRecipe<C extends RecipeContext<I>, I extends RecipeInstance> implements MachineRecipe<C, I>, CacheableMachineRecipe {

    protected final Identifier id;
    protected final List<CountableIngredient> inputs;
    protected final List<FluidVolume> fluidInputs;

    protected final List<ItemStack> outputs;
    protected final List<ChanceEntry> chancedOutputs;
    protected final List<FluidVolume> fluidOutputs;
    protected final int duration;

    protected final List<ItemStack> combinedOutputs;

    public BasicMachineRecipe(Identifier id,
                              List<CountableIngredient> inputs, List<FluidVolume> fluidInputs,
                              List<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
                              List<FluidVolume> fluidOutputs,
                              int duration) {
        this.id = id;
        this.inputs = ImmutableList.copyOf(inputs);
        this.fluidInputs = ImmutableList.copyOf(fluidInputs);
        this.outputs = ImmutableList.copyOf(outputs);
        this.chancedOutputs = ImmutableList.copyOf(chancedOutputs);
        this.fluidOutputs = ImmutableList.copyOf(fluidOutputs);
        this.duration = duration;

        ArrayList<ItemStack> combinedOutputs = new ArrayList<>(outputs);
        for (ChanceEntry chanceEntry : chancedOutputs) {
            combinedOutputs.add(chanceEntry.getItemStack());
        }
        this.combinedOutputs = InventoryUtil.compactItemStacks(combinedOutputs);
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.BASIC_MACHINE_RECIPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends RecipeContext<?>> getMinimumSupportedContextClass() {
        return (Class<? extends RecipeContext<?>>) RecipeContext.class;
    }

    public List<CountableIngredient> getInputs() {
        return inputs;
    }

    public List<FluidVolume> getFluidInputs() {
        return fluidInputs;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public List<ChanceEntry> getChancedOutputs() {
        return chancedOutputs;
    }

    public List<FluidVolume> getFluidOutputs() {
        return fluidOutputs;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean matches(C context) {
        if (!RecipeUtil.matchesItems(context.getItemInventory(), inputs)) {
            return false;
        }
        return RecipeUtil.matchesFluids(context.getFluidInventory(), fluidInputs);
    }

    @Override
    public I startRecipe(C context) {
        RecipeUtil.consumeItems(context.getItemInventory(), this.inputs);
        RecipeUtil.consumeFluids(context.getFluidInventory(), this.fluidInputs);

        I instance = context.createRecipeInstance(this);
        instance.setRecipeDuration(this.duration);
        return instance;
    }

    @Override
    public boolean canFitOutputs(C context, I instance) {
        if (!InventoryUtil.insertItemsIntoInventory(context.getOutputItemInventory(), combinedOutputs, Simulation.SIMULATE)) {
            return false;
        }
        return InventoryUtil.insertFluidIntoInventory(context.getOutputFluidInventory(), fluidOutputs, Simulation.SIMULATE);
    }

    @Override
    public void addOutputs(C context, I instance) {
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

    public static final class Serializer extends BasicMachineRecipeSerializer<BasicMachineRecipe<?, ?>> {

        private static BasicMachineRecipe<?, ?> createFromContext(RecipeCreationContext context) {
            return new BasicMachineRecipe<>(context.id,
                    context.inputs, context.fluidInputs,
                    context.outputs, context.chancedOutputs, context.fluidOutputs,
                    context.duration);
        }

        @Override
        protected BasicMachineRecipe<?, ?> createRecipeFromJson(RecipeCreationContext context, JsonObject json) {
            return createFromContext(context);
        }

        @Override
        protected BasicMachineRecipe<?, ?> createRecipeFromPacket(RecipeCreationContext context, PacketByteBuf buf) {
            return createFromContext(context);
        }

        @Override
        protected void writeRecipeDataToPacket(BasicMachineRecipe<?, ?> recipe, PacketByteBuf buf) {
        }
    }
}
