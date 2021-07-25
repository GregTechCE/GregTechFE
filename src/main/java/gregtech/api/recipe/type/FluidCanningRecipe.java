package gregtech.api.recipe.type;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.*;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.FixedItemInvView;
import alexiil.mc.lib.attributes.misc.NullVariant;
import alexiil.mc.lib.attributes.misc.Ref;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gregtech.api.recipe.DynamicMachineRecipe;
import gregtech.api.recipe.RecipeSerializer;
import gregtech.api.recipe.context.ElectricMachineContext;
import gregtech.api.recipe.MachineRecipe;
import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;
import gregtech.api.recipe.context.RecipeContext;
import gregtech.api.recipe.util.CanningRecipeData;
import gregtech.api.recipe.util.RecipeDataTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class FluidCanningRecipe<C extends ElectricMachineContext<I>, I extends ElectricMachineRecipeInstance> implements MachineRecipe<C, I>, DynamicMachineRecipe {

    protected final Identifier id;
    protected final int duration;
    protected final int EUt;

    public FluidCanningRecipe(Identifier id, int duration, int EUt) {
        this.id = id;
        this.duration = duration;
        this.EUt = EUt;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.FLUID_CANNING_RECIPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends RecipeContext<?>> getMinimumSupportedContextClass() {
        return (Class<? extends RecipeContext<?>>) ElectricMachineContext.class;
    }

    public int getDuration() {
        return duration;
    }

    public int getEUt() {
        return EUt;
    }

    @Override
    public boolean matches(C context) {
        if (context.getMaxVoltage() < this.EUt) {
            return false;
        }
        CanningRecipeTargetInfo recipeTargetInfo = findMatchingCanningRecipe(context.getItemInventory(), context.getFluidInventory());
        return recipeTargetInfo != null;
    }

    @Override
    public I startRecipe(C context) {
        CanningRecipeTargetInfo recipeTargetInfo = findMatchingCanningRecipe(context.getItemInventory(), context.getFluidInventory());
        Preconditions.checkNotNull(recipeTargetInfo);

        FixedItemInv itemInputInventory = context.getItemInventory();

        //Decrement container stack by one, since we are filling one container
        ItemStack containerStack = itemInputInventory.extractStack(recipeTargetInfo.slotIndex, null, ItemStack.EMPTY, 1, Simulation.ACTION);
        Ref<ItemStack> stackRef = new Ref<>(containerStack);
        FluidVolume fluidToInsert = FluidVolumeUtil.EMPTY;

        //Consume fluids from the input tank if we are performing insertion
        if (!recipeTargetInfo.isExtraction) {
            FixedFluidInv fluidInputInventory = context.getFluidInventory();
            SingleFluidTank tankAtIndex = fluidInputInventory.getTank(recipeTargetInfo.tankIndex);

            FluidInsertable insertable = FluidAttributes.INSERTABLE.get(stackRef);
            FluidVolumeUtil.move(tankAtIndex, insertable, Simulation.ACTION);
        } else {
            //For extraction, we need to simulate the fluid move to determine how much fluid can be actually moved
            FixedFluidInv fluidOutputInventory = context.getOutputFluidInventory();

            FluidExtractable extractable = FluidAttributes.EXTRACTABLE.get(stackRef);
            fluidToInsert = FluidVolumeUtil.move(extractable, fluidOutputInventory.getInsertable(), Simulation.SIMULATE);

            //Then we actually force extractable to drain that amount of fluid, so we know the resulting item stack
            extractable.extract(fluidToInsert.getFluidKey(), fluidToInsert.getAmount_F());
        }

        I instance = context.createRecipeInstance(this);

        CanningRecipeData canningRecipeData = new CanningRecipeData(stackRef.get(), fluidToInsert);
        instance.setRecipeData(RecipeDataTypes.CANNING_RECIPE_DATA, canningRecipeData);

        instance.setRecipeEUt(this.EUt);
        instance.setRecipeDuration(this.duration);

        return instance;
    }

    @Override
    public boolean canFitOutputs(C context, I instance) {
        CanningRecipeData recipeData = instance.getRecipeData(RecipeDataTypes.CANNING_RECIPE_DATA).orElseThrow();

        FixedFluidInv fluidOutputInventory = context.getOutputFluidInventory();
        FluidVolume fluidToInsert = recipeData.getFluidToInsert();

        if (!fluidToInsert.isEmpty()) {
            FluidVolume excess = fluidOutputInventory.getInsertable().attemptInsertion(fluidToInsert, Simulation.SIMULATE);
            if (!excess.isEmpty()) {
                return false;
            }
        }

        FixedItemInv itemOutputInventory = context.getOutputItemInventory();
        ItemStack containerStack = recipeData.getContainerStack();

        if (!containerStack.isEmpty()) {
            ItemStack excessStack = itemOutputInventory.getInsertable().attemptInsertion(containerStack, Simulation.SIMULATE);
            return excessStack.isEmpty();
        }
        return true;
    }

    @Override
    public void addOutputs(C context, I instance) {
        CanningRecipeData recipeData = instance.getRecipeData(RecipeDataTypes.CANNING_RECIPE_DATA).orElseThrow();

        //Insert resulting fluid if we were draining the container
        FixedFluidInv fluidOutputInventory = context.getOutputFluidInventory();
        if (!recipeData.getFluidToInsert().isEmpty()) {
            fluidOutputInventory.getInsertable().insert(recipeData.getFluidToInsert());
        }

        //Insert resulting container stack, if it's not empty of course
        FixedItemInv itemOutputInventory = context.getOutputItemInventory();
        if (!recipeData.getContainerStack().isEmpty()) {
            itemOutputInventory.getInsertable().insert(recipeData.getContainerStack());
        }
    }

    @Override
    public boolean canInputFluid(FluidKey fluidKey) {
        return true;
    }

    @Override
    public boolean canInputItem(ItemStack itemStack) {
        FluidExtractable extractable = FluidAttributes.EXTRACTABLE.get(itemStack);
        if (!(extractable instanceof NullVariant)) {
            return true;
        }

        FluidInsertable insertable = FluidAttributes.INSERTABLE.get(itemStack);
        return !(insertable instanceof NullVariant);
    }

    @Nullable
    private CanningRecipeTargetInfo findMatchingCanningRecipe(FixedItemInvView itemInvView, FixedFluidInvView fluidInvView) {
        for (int slotIndex = 0; slotIndex < itemInvView.getSlotCount(); slotIndex++) {
            ItemStack itemStack = itemInvView.getInvStack(slotIndex);

            //Skip over empty slots in the inventory
            if (itemStack.isEmpty()) {
                continue;
            }

            //First, attempt to perform the extraction. If we can extract anything, we are good to go
            FluidExtractable extractable = FluidAttributes.EXTRACTABLE.get(itemStack);

            if (!(extractable instanceof NullVariant)) {
                FluidVolume extractedVolume = extractable.attemptAnyExtraction(FluidAmount.A_MILLION, Simulation.SIMULATE);
                if (!extractedVolume.isEmpty()) {
                    return new CanningRecipeTargetInfo(slotIndex, -1, true);
                }
            }

            FluidInsertable insertable = FluidAttributes.INSERTABLE.get(itemStack);

            //If we don't have insertable of extractable, keep going
            if (insertable instanceof NullVariant) {
                continue;
            }

            //Then, attempt to simulate the insertion by looping through the tanks
            for (int tankIndex = 0; tankIndex < fluidInvView.getTankCount(); tankIndex++) {
                FluidVolume tankFluid = fluidInvView.getInvFluid(tankIndex);

                //No point in attempting to insert an empty fluid
                if (tankFluid.isEmpty()) {
                    continue;
                }

                //If we successfully inserted something, e.g. excess stack does not the have same amount as original one
                FluidVolume excessStack = insertable.attemptInsertion(tankFluid, Simulation.SIMULATE);
                if (!excessStack.getAmount_F().equals(tankFluid.getAmount_F())) {

                    return new CanningRecipeTargetInfo(slotIndex, tankIndex, false);
                }
            }
        }
        return null;
    }

    private static class CanningRecipeTargetInfo {
        private final int slotIndex;
        private final int tankIndex;
        private final boolean isExtraction;

        public CanningRecipeTargetInfo(int slotIndex, int tankIndex, boolean isExtraction) {
            this.slotIndex = slotIndex;
            this.tankIndex = tankIndex;
            this.isExtraction = isExtraction;
        }
    }

    public static final class Serializer implements RecipeSerializer<FluidCanningRecipe<?, ?>> {

        @Override
        public FluidCanningRecipe<?, ?> read(Identifier id, JsonObject json) {
            int duration = JsonHelper.getInt(json, "duration");
            int recipeEUt = JsonHelper.getInt(json, "eu_per_tick");

            if (duration <= 0) {
                throw new JsonParseException("Invalid duration: should be positive");
            }
            if (recipeEUt <= 0) {
                throw new JsonParseException("Invalid eu_per_tick: should be positive");
            }
            return new FluidCanningRecipe<>(id, duration, recipeEUt);
        }

        @Override
        public FluidCanningRecipe<?, ?> read(Identifier id, PacketByteBuf buf) {
            int duration = buf.readVarInt();
            int recipeEUt = buf.readVarInt();

            return new FluidCanningRecipe<>(id, duration, recipeEUt);
        }

        @Override
        public void write(PacketByteBuf buf, FluidCanningRecipe<?, ?> recipe) {
            buf.writeVarInt(recipe.getDuration());
            buf.writeVarInt(recipe.getEUt());
        }
    }
}
