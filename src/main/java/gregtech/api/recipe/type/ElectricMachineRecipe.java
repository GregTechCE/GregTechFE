package gregtech.api.recipe.type;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gregtech.api.recipe.RecipeSerializer;
import gregtech.api.recipe.context.ElectricMachineContext;
import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;
import gregtech.api.recipe.context.RecipeContext;
import gregtech.api.recipe.util.ChanceEntry;
import gregtech.api.recipe.util.CountableIngredient;
import gregtech.api.recipe.util.OverclockResult;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.List;

public class ElectricMachineRecipe<C extends ElectricMachineContext<I>, I extends ElectricMachineRecipeInstance> extends BasicMachineRecipe<C, I> {

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

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends RecipeContext<?>> getMinimumSupportedContextClass() {
        return (Class<? extends RecipeContext<?>>) ElectricMachineContext.class;
    }

    @Override
    public boolean matches(C context) {
        if (context.getMaxVoltage() < this.EUt) {
            return false;
        }
        return super.matches(context);
    }

    @Override
    public I startRecipe(C context) {
        I instance = super.startRecipe(context);
        OverclockResult overclockResult = context.applyOverclocking(this.EUt, instance.getRecipeDuration());

        instance.setRecipeDuration(overclockResult.getDuration());
        instance.setRecipeEUt(overclockResult.getEUt());
        return instance;
    }

    public static final class Serializer extends BasicMachineRecipeSerializer<ElectricMachineRecipe<?, ?>> {

        private static ElectricMachineRecipe<?, ?> createFromContext(RecipeCreationContext context, int EUt) {
            return new ElectricMachineRecipe<>(context.id,
                    context.inputs, context.fluidInputs,
                    context.outputs, context.chancedOutputs, context.fluidOutputs,
                    context.duration, EUt);
        }

        @Override
        protected ElectricMachineRecipe<?, ?> createRecipeFromJson(RecipeCreationContext context, JsonObject json) {
            int recipeEUt = JsonHelper.getInt(json, "eu_per_tick");

            if (recipeEUt <= 0) {
                throw new JsonParseException("Invalid eu_per_tick: should be positive");
            }
            return createFromContext(context, recipeEUt);
        }

        @Override
        protected void writeRecipeDataToPacket(ElectricMachineRecipe<?, ?> recipe, PacketByteBuf buf) {
            buf.writeVarInt(recipe.getEUt());
        }

        @Override
        protected ElectricMachineRecipe<?, ?> createRecipeFromPacket(RecipeCreationContext context, PacketByteBuf buf) {
            int recipeEUt = buf.readVarInt();
            return createFromContext(context, recipeEUt);
        }
    }
}
