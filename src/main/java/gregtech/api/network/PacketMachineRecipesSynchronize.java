package gregtech.api.network;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import gregtech.api.GTValues;
import gregtech.api.recipe.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class PacketMachineRecipesSynchronize implements NetworkPacket {

    public static final Identifier CHANNEL = new Identifier(GTValues.MODID, "machine_recipe_sync");

    private final Map<MachineRecipeType, List<MachineRecipe<?, ?>>> recipes;

    public PacketMachineRecipesSynchronize(GTRecipeManager recipeManager) {
        ImmutableMap.Builder<MachineRecipeType, List<MachineRecipe<?, ?>>> builder = ImmutableMap.builder();

        for (MachineRecipeType recipeType : MachineRecipeType.REGISTRY.stream().toList()) {
            RecipeMap recipeMap = recipeManager.getRecipesOfType(recipeType);
            if (recipeMap != null) {
                builder.put(recipeType, recipeMap.getRecipes());
            }
        }
        this.recipes = builder.build();
    }

    public PacketMachineRecipesSynchronize(PacketByteBuf buffer) {
        this.recipes = buffer.readMap(
                buf -> MachineRecipeType.REGISTRY.get(buf.readVarInt()),

        (buf) -> buf.readList(byteBuf -> {
            RecipeSerializer<?> recipeSerializer = RecipeSerializer.REGISTRY.get(byteBuf.readVarInt());
            Preconditions.checkNotNull(recipeSerializer, "recipeSerializer not found for recipe");

            Identifier id = byteBuf.readIdentifier();
            return recipeSerializer.read(id, byteBuf);
        }));
    }

    public Map<MachineRecipeType, List<MachineRecipe<?, ?>>> getRecipes() {
        return recipes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void writeBuffer(PacketByteBuf buffer) {
        buffer.writeMap(this.recipes,
                (buf, machineRecipeType) -> buf.writeVarInt(MachineRecipeType.REGISTRY.getRawId(machineRecipeType)),

        (buf, recipes) -> buf.writeCollection(recipes, (byteBuf, recipe) -> {
            RecipeSerializer<?> recipeSerializer = recipe.getSerializer();

            byteBuf.writeIdentifier(recipe.getId());
            ((RecipeSerializer<MachineRecipe<?, ?>>) recipeSerializer).write(byteBuf, recipe);
        }));
    }

    @Override
    public Identifier getChannel() {
        return CHANNEL;
    }
}
