package gregtech.api.recipes.recipes;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gregtech.api.recipes.RecipeSerializer;
import gregtech.api.recipes.context.ElectricMachineContext;
import gregtech.api.recipes.util.ChanceEntry;
import gregtech.api.recipes.util.CountableIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.List;

public class ElectricMachineRecipe<C extends ElectricMachineContext> extends BasicMachineRecipe<C> {

    protected final int EUt;

    public ElectricMachineRecipe(Identifier id,
                                 List<CountableIngredient> inputs, List<FluidVolume> fluidInputs,
                                 List<ItemStack> outputs, List<ChanceEntry> chancedOutputs,
                                 List<FluidVolume> fluidOutputs,
                                 int duration, int EUt) {
        super(id, inputs, fluidInputs, outputs, chancedOutputs, fluidOutputs, duration);
        this.EUt = EUt;
    }

    public int getEUt() {
        return EUt;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.ELECTRIC_MACHINE_RECIPE;
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

    public static final class Serializer extends BasicMachineRecipeSerializer<ElectricMachineRecipe<?>> {

        private static ElectricMachineRecipe<?> createFromContext(RecipeCreationContext context, int EUt) {
            return new ElectricMachineRecipe<>(context.id,
                    context.inputs, context.fluidInputs,
                    context.outputs, context.chancedOutputs, context.fluidOutputs,
                    context.duration, EUt);
        }

        @Override
        protected ElectricMachineRecipe<?> createRecipeFromJson(RecipeCreationContext context, JsonObject json) {
            int recipeEUt = JsonHelper.getInt(json, "eu_per_tick");

            if (recipeEUt <= 0) {
                throw new JsonParseException("Invalid eu_per_tick: should be positive");
            }
            return createFromContext(context, recipeEUt);
        }

        @Override
        protected void writeRecipeDataToPacket(ElectricMachineRecipe<?> recipe, PacketByteBuf buf) {
            buf.writeVarInt(recipe.getEUt());
        }

        @Override
        protected ElectricMachineRecipe<?> createRecipeFromPacket(RecipeCreationContext context, PacketByteBuf buf) {
            int recipeEUt = buf.readVarInt();
            return createFromContext(context, recipeEUt);
        }
    }
}
