package gregtech.api.recipe.util;

import gregtech.api.GTValues;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

public class RecipeDataTypes {

    public static RecipeDataType<CanningRecipeData> CANNING_RECIPE_DATA;

    public static void ensureInitialized() {
    }

    private static <T> RecipeDataType<T> register(String name, Class<T> valueClass, Function<T, NbtCompound> serializer, Function<NbtCompound, T> deserializer) {
        return Registry.register(RecipeDataType.REGISTRY, new Identifier(GTValues.MODID, name), new RecipeDataType<>(valueClass, serializer, deserializer));
    }

    static {
        CANNING_RECIPE_DATA = register("canning_recipe_data", CanningRecipeData.class, CanningRecipeData::writeTag, CanningRecipeData::new);
    }
}
