package gregtech.api.recipe.type;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gregtech.api.recipe.RecipeSerializer;
import gregtech.api.recipe.context.EBFMachineContext;
import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;
import gregtech.api.recipe.context.RecipeContext;
import gregtech.api.recipe.util.ChanceEntry;
import gregtech.api.recipe.util.CountableIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.List;

public class BlastFurnaceRecipe<C extends EBFMachineContext<I>, I extends ElectricMachineRecipeInstance> extends ElectricMachineRecipe<C, I> {

    protected final int requiredTemperature;

    public BlastFurnaceRecipe(Identifier id,
                              List<CountableIngredient> inputs, List<FluidVolume> fluidInputs,
                              List<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
                              List<FluidVolume> fluidOutputs,
                              int duration, int EUt, int requiredTemperature) {
        super(id, inputs, fluidInputs, outputs, chancedOutputs, fluidOutputs, duration, EUt);
        this.requiredTemperature = requiredTemperature;
    }

    public int getRequiredTemperature() {
        return requiredTemperature;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.BLAST_FURNACE_RECIPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends RecipeContext<?>> getMinimumSupportedContextClass() {
        return (Class<? extends RecipeContext<?>>) EBFMachineContext.class;
    }

    @Override
    public boolean matches(C context) {
        if (context.getMaxTemperature() < this.requiredTemperature) {
            return false;
        }
        return super.matches(context);
    }

    public static final class Serializer extends BasicMachineRecipeSerializer<BlastFurnaceRecipe<?, ?>> {

        private static BlastFurnaceRecipe<?, ?> createFromContext(RecipeCreationContext context, int recipeEUt, int temperature) {
            return new BlastFurnaceRecipe<>(context.id,
                    context.inputs, context.fluidInputs,
                    context.outputs, context.chancedOutputs, context.fluidOutputs,
                    context.duration, recipeEUt, temperature);
        }

        @Override
        protected BlastFurnaceRecipe<?, ?> createRecipeFromJson(RecipeCreationContext context, JsonObject json) {
            int recipeEUt = JsonHelper.getInt(json, "eu_per_tick");
            int temperature = JsonHelper.getInt(json, "temperature");

            if (recipeEUt <= 0) {
                throw new JsonParseException("Invalid eu_per_tick: should be positive");
            }
            if (temperature <= 0) {
                throw new JsonParseException("Invalid temperature: should be positive");
            }
            return createFromContext(context, recipeEUt, temperature);
        }

        @Override
        protected BlastFurnaceRecipe<?, ?> createRecipeFromPacket(RecipeCreationContext context, PacketByteBuf buf) {
            int recipeEUt = buf.readVarInt();
            int temperature = buf.readVarInt();

            return createFromContext(context, recipeEUt, temperature);
        }

        @Override
        protected void writeRecipeDataToPacket(BlastFurnaceRecipe<?, ?> recipe, PacketByteBuf buf) {
            buf.writeVarInt(recipe.getEUt());
            buf.writeVarInt(recipe.getRequiredTemperature());
        }
    }
}
