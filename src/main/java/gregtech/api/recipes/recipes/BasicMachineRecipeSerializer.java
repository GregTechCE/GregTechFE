package gregtech.api.recipes.recipes;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gregtech.api.recipes.RecipeSerializer;
import gregtech.api.recipes.util.ChanceEntry;
import gregtech.api.recipes.util.CountableIngredient;
import gregtech.api.util.JsonUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicMachineRecipeSerializer<T extends BasicMachineRecipe<?>> implements RecipeSerializer<T> {

    protected static class RecipeCreationContext {
        final Identifier id;
        List<CountableIngredient> inputs = Collections.emptyList();
        List<FluidVolume> fluidInputs = Collections.emptyList();

        List<ItemStack> outputs = Collections.emptyList();
        List<ChanceEntry> chancedOutputs = Collections.emptyList();
        List<FluidVolume> fluidOutputs = Collections.emptyList();
        int duration = 0;

        public RecipeCreationContext(Identifier id) {
            this.id = id;
        }
    }

    protected abstract void writeRecipeDataToPacket(T recipe, PacketByteBuf buf);
    
    protected abstract T createRecipeFromPacket(RecipeCreationContext context, PacketByteBuf buf);

    protected abstract T createRecipeFromJson(RecipeCreationContext context, JsonObject json);

    @Override
    public final T read(Identifier id, JsonObject json) {
        RecipeCreationContext context = new RecipeCreationContext(id);

        JsonArray inputsArray = JsonHelper.getArray(json, "inputs", null);
        if (inputsArray != null) {
            context.inputs = Streams.stream(inputsArray)
                    .map(element -> JsonHelper.asObject(element, "inputs array element"))
                    .map(CountableIngredient::fromJson)
                    .collect(Collectors.toList());
        }

        JsonArray fluidInputsArray = JsonHelper.getArray(json, "fluid_inputs", null);
        if (fluidInputsArray != null) {
            context.fluidInputs = Streams.stream(fluidInputsArray)
                    .map(element -> JsonHelper.asObject(element, "fluid_inputs array element"))
                    .map(FluidVolume::fromJson)
                    .collect(Collectors.toList());
        }

        JsonArray outputsArray = JsonHelper.getArray(json, "outputs", null);
        if (outputsArray != null) {
            context.outputs = Streams.stream(outputsArray)
                    .map(element -> JsonHelper.asObject(element, "outputs array element"))
                    .map(JsonUtil::readItemStack)
                    .collect(Collectors.toList());
        }

        JsonArray chancedOutputsArray = JsonHelper.getArray(json, "chanced_outputs", null);
        if (chancedOutputsArray != null) {
            context.chancedOutputs = Streams.stream(chancedOutputsArray)
                    .map(element -> JsonHelper.asObject(element, "chanced_outputs array element"))
                    .map(ChanceEntry::fromJson)
                    .collect(Collectors.toList());
        }

        JsonArray fluidOutputsArray = JsonHelper.getArray(json, "fluid_outputs", null);
        if (fluidOutputsArray != null) {
            context.fluidOutputs = Streams.stream(fluidOutputsArray)
                    .map(element -> JsonHelper.asObject(element, "fluid_outputs array element"))
                    .map(FluidVolume::fromJson)
                    .collect(Collectors.toList());
        }

        context.duration = JsonHelper.getInt(json, "duration");
        if (context.duration <= 0) {
            throw new JsonParseException("Invalid duration: should be positive");
        }

        return createRecipeFromJson(context, json);
    }

    @Override
    public final T read(Identifier id, PacketByteBuf buf) {
        RecipeCreationContext context = new RecipeCreationContext(id);

        context.inputs = buf.readList(CountableIngredient::fromPacket);
        context.fluidInputs = buf.readList(BasicMachineRecipeSerializer::fluidVolumeFromMCBuffer);

        context.outputs = buf.readList(PacketByteBuf::readItemStack);
        context.chancedOutputs = buf.readList(ChanceEntry::fromPacket);
        context.fluidOutputs = buf.readList(BasicMachineRecipeSerializer::fluidVolumeFromMCBuffer);

        context.duration = buf.readVarInt();
        return createRecipeFromPacket(context, buf);
    }

    @Override
    public final void write(PacketByteBuf buf, T recipe) {
        buf.writeCollection(recipe.getInputs(), (buffer, ingredient) -> ingredient.toPacket(buffer));
        buf.writeCollection(recipe.getFluidInputs(), (buffer, fluid) -> fluid.toMcBuffer(buffer));

        buf.writeCollection(recipe.getOutputs(), PacketByteBuf::writeItemStack);
        buf.writeCollection(recipe.getChancedOutputs(), (buffer, output) -> output.toPacket(buffer));
        buf.writeCollection(recipe.getFluidOutputs(), (buffer, fluid) -> fluid.toMcBuffer(buffer));
        
        buf.writeVarInt(recipe.getDuration());
        writeRecipeDataToPacket(recipe, buf);
    }

    public static FluidVolume fluidVolumeFromMCBuffer(PacketByteBuf buf) {
        try {
            return FluidVolume.fromMcBuffer(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
