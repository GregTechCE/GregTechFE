package gregtech.api.recipes.context;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public interface RecipeContext {

    Random getRandom();
    int getTierForBoosting();

    <T> void setRecipeData(String key, T data, Function<T, NbtCompound> serializer);
    <T> Optional<T> getRecipeData(String key, Function<NbtCompound, T> deserializer);

    void setRemainingRecipeDuration(int recipeDuration);

    FixedItemInv getItemInventory();
    FixedFluidInv getFluidInventory();


    FixedItemInv getOutputItemInventory();
    FixedFluidInv getOutputFluidInventory();
}
