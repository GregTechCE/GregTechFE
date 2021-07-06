package gregtech.api.recipes.recipes;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gregtech.api.recipes.RecipeSerializer;
import gregtech.api.recipes.context.PBFMachineContext;
import gregtech.api.recipes.context.RecipeContext;
import gregtech.api.recipes.util.ChanceEntry;
import gregtech.api.recipes.util.CountableIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.List;

public class PBFRecipe<C extends PBFMachineContext> extends BasicMachineRecipe<C> {

    protected final int fuelCost;

    public PBFRecipe(Identifier id,
                     List<CountableIngredient> inputs, List<FluidVolume> fluidInputs,
                     List<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
                     List<FluidVolume> fluidOutputs,
                     int duration, int fuelCost) {
        super(id, inputs, fluidInputs, outputs, chancedOutputs, fluidOutputs, duration);
        this.fuelCost = fuelCost;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.PRIMITIVE_BLAST_FURNACE_RECIPE;
    }

    @Override
    public Class<? extends RecipeContext> getMinimumSupportedContextClass() {
        return PBFMachineContext.class;
    }

    public int getFuelCost() {
        return fuelCost;
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

    public static final class Serializer extends BasicMachineRecipeSerializer<PBFRecipe<?>> {

        private static PBFRecipe<?> createFromContext(RecipeCreationContext context, int fuelCost) {
            return new PBFRecipe<>(context.id,
                    context.inputs, context.fluidInputs,
                    context.outputs, context.chancedOutputs, context.fluidOutputs,
                    context.duration, fuelCost);
        }

        @Override
        protected PBFRecipe<?> createRecipeFromJson(RecipeCreationContext context, JsonObject json) {
            int fuelCost = JsonHelper.getInt(json, "fuel_cost");

            if (fuelCost <= 0) {
                throw new JsonParseException("Invalid fuel_cost: must be positive");
            }
            return createFromContext(context, fuelCost);
        }

        @Override
        protected PBFRecipe<?> createRecipeFromPacket(RecipeCreationContext context, PacketByteBuf buf) {
            int fuelCost = buf.readVarInt();
            return createFromContext(context, fuelCost);
        }

        @Override
        protected void writeRecipeDataToPacket(PBFRecipe<?> recipe, PacketByteBuf buf) {
            buf.writeVarInt(recipe.getFuelCost());
        }
    }
}
