package gregtech.api.recipes.recipes;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.*;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.FixedItemInvView;
import alexiil.mc.lib.attributes.misc.NullVariant;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gregtech.api.recipes.RecipeSerializer;
import gregtech.api.recipes.context.ElectricMachineContext;
import gregtech.api.recipes.MachineRecipe;
import gregtech.api.recipes.context.RecipeContext;
import gregtech.api.util.ref.SimpleReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class FluidCanningRecipe<C extends ElectricMachineContext> implements MachineRecipe<C> {

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

    @Override
    public Class<? extends RecipeContext> getMinimumSupportedContextClass() {
        return ElectricMachineContext.class;
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
    public boolean canFitOutputs(C context) {
        CanningRecipeTargetInfo recipeTargetInfo = findMatchingCanningRecipe(context.getItemInventory(), context.getFluidInventory());
        if (recipeTargetInfo == null) {
            return false;
        }

        //Extract a single container and attempt to fill or drain it
        FixedItemInv itemInputInventory = context.getItemInventory();
        ItemStack containerStack = itemInputInventory.extractStack(recipeTargetInfo.slotIndex, null, ItemStack.EMPTY, 1, Simulation.SIMULATE);
        if (containerStack.isEmpty()) {
            return false;
        }

        SimpleReference<ItemStack> stackRef = new SimpleReference<>(containerStack);
        FluidVolume fluidMoved;

        //Try to simulate fluid movement and skip the recipe if we cannot extract or insert anything,
        //for example because either input or output tanks are empty
        if (recipeTargetInfo.isExtraction) {
            FixedFluidInv fluidOutputInv = context.getOutputFluidInventory();
            FluidExtractable extractable = FluidAttributes.EXTRACTABLE.get(stackRef);

            fluidMoved = FluidVolumeUtil.move(extractable, fluidOutputInv.getInsertable(), Simulation.SIMULATE);

            //Now we need to actually drain the fluid from the container to know the resulting stack
            extractable.extract(fluidMoved.getFluidKey(), fluidMoved.getAmount_F());
        } else {
            FixedFluidInv fluidInputInventory = context.getFluidInventory();
            SingleFluidTank tankAtIndex = fluidInputInventory.getTank(recipeTargetInfo.tankIndex);

            FluidInsertable insertable = FluidAttributes.INSERTABLE.get(stackRef);
            fluidMoved = FluidVolumeUtil.move(tankAtIndex, insertable, Simulation.SIMULATE);

            //We need to actually fill the container to determine the resulting item
            insertable.insert(fluidMoved);
        }

        if (fluidMoved.isEmpty()) {
            return false;
        }

        //We can actually start the recipe only if we have space for the resulting container
        FixedItemInv itemOutputInv = context.getOutputItemInventory();
        ItemStack excessStack = itemOutputInv.getInsertable().attemptInsertion(stackRef.get(), Simulation.SIMULATE);

        return excessStack.isEmpty();
    }

    @Override
    public void onStarted(C context) {
        CanningRecipeTargetInfo recipeTargetInfo = findMatchingCanningRecipe(context.getItemInventory(), context.getFluidInventory());
        Preconditions.checkNotNull(recipeTargetInfo);

        FixedItemInv itemInputInventory = context.getItemInventory();

        //Decrement container stack by one, since we are filling one container
        ItemStack containerStack = itemInputInventory.extractStack(recipeTargetInfo.slotIndex, null, ItemStack.EMPTY, 1, Simulation.ACTION);
        SimpleReference<ItemStack> stackRef = new SimpleReference<>(containerStack);
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

        CanningRecipeData canningRecipeData = new CanningRecipeData(stackRef.get(), fluidToInsert);
        context.setRecipeData("CanningRecipeData", canningRecipeData, CanningRecipeData::writeTag);

        context.setRecipeEUt(this.EUt);
        context.setRemainingRecipeDuration(this.duration);
    }

    @Override
    public void addOutputs(C context) {
        CanningRecipeData recipeData = context.getRecipeData("CanningRecipeData", CanningRecipeData::new).orElseThrow();

        //Insert resulting fluid if we were draining the container
        FixedFluidInv fluidOutputInventory = context.getOutputFluidInventory();
        if (!recipeData.fluidToInsert.isEmpty()) {
            fluidOutputInventory.getInsertable().insert(recipeData.fluidToInsert);
        }

        //Insert resulting container stack, if it's not empty of course
        FixedItemInv itemOutputInventory = context.getOutputItemInventory();
        if (!recipeData.containerStack.isEmpty()) {
            itemOutputInventory.getInsertable().insert(recipeData.containerStack);
        }
    }

    private static class CanningRecipeData {

        private final ItemStack containerStack;
        private final FluidVolume fluidToInsert;

        public CanningRecipeData(ItemStack containerStack, FluidVolume fluidToInsert) {
            this.containerStack = containerStack;
            this.fluidToInsert = fluidToInsert;
        }

        public CanningRecipeData(NbtCompound compound) {
            this.containerStack = ItemStack.fromNbt(compound.getCompound("Container"));
            this.fluidToInsert = FluidVolume.fromTag(compound.getCompound("Fluid"));
        }

        public NbtCompound writeTag() {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.put("Container", this.containerStack.writeNbt(new NbtCompound()));
            nbtCompound.put("Fluid", this.fluidToInsert.toTag());

            return nbtCompound;
        }
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

    public static final class Serializer implements RecipeSerializer<FluidCanningRecipe<?>> {

        @Override
        public FluidCanningRecipe<?> read(Identifier id, JsonObject json) {
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
        public FluidCanningRecipe<?> read(Identifier id, PacketByteBuf buf) {
            int duration = buf.readVarInt();
            int recipeEUt = buf.readVarInt();

            return new FluidCanningRecipe<>(id, duration, recipeEUt);
        }

        @Override
        public void write(PacketByteBuf buf, FluidCanningRecipe<?> recipe) {
            buf.writeVarInt(recipe.getDuration());
            buf.writeVarInt(recipe.getEUt());
        }
    }
}
