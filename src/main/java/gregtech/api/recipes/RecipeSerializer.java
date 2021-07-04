package gregtech.api.recipes;

import com.google.gson.JsonObject;
import gregtech.api.GTValues;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface RecipeSerializer<T extends MachineRecipe<?>> {

    @SuppressWarnings("unchecked")
    Registry<RecipeSerializer<?>> REGISTRY = FabricRegistryBuilder
            .createSimple((Class<RecipeSerializer<?>>) (Object) RecipeSerializer.class, new Identifier(GTValues.MODID, "recipe_serializers"))
            .buildAndRegister();

    T read(Identifier id, JsonObject json);

    T read(Identifier id, PacketByteBuf buf);

    void write(PacketByteBuf buf, T recipe);
}
