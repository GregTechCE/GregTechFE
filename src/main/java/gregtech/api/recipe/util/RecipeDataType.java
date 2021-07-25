package gregtech.api.recipe.util;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

public class RecipeDataType<T> {

    @SuppressWarnings("unchecked")
    public static final Registry<RecipeDataType<?>> REGISTRY = FabricRegistryBuilder.createSimple(
            (Class<RecipeDataType<?>>) (Object) RecipeDataType.class, new Identifier(GTValues.MODID, "recipe_data_types"))
            .buildAndRegister();

    private final Class<T> valueClass;
    private final Function<T, NbtCompound> serializer;
    private final Function<NbtCompound, T> deserializer;

    public RecipeDataType(Class<T> valueClass, Function<T, NbtCompound> serializer, Function<NbtCompound, T> deserializer) {
        Preconditions.checkNotNull(valueClass, "valueClass");
        Preconditions.checkNotNull(serializer, "serializer");
        Preconditions.checkNotNull(deserializer, "deserializer");

        this.valueClass = valueClass;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public Identifier getId() {
        return Preconditions.checkNotNull(REGISTRY.getId(this));
    }

    public T castRecipeData(Object recipeData) {
        return this.valueClass.cast(recipeData);
    }

    public NbtCompound serializeRecipeData(T data) {
        return this.serializer.apply(data);
    }

    public T deserializeRecipeData(NbtCompound nbt) {
        return this.deserializer.apply(nbt);
    }

    @Override
    public String toString() {
        Identifier id = REGISTRY.getId(this);
        return id != null ? id.toString() : super.toString();
    }
}
