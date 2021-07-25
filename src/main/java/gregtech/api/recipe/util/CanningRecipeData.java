package gregtech.api.recipe.util;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class CanningRecipeData {

    private final ItemStack containerStack;
    private final FluidVolume fluidToInsert;

    public CanningRecipeData(ItemStack containerStack, FluidVolume fluidToInsert) {
        this.containerStack = containerStack;
        this.fluidToInsert = fluidToInsert;
    }

    public CanningRecipeData(NbtCompound compound) {
        this.containerStack = ItemStack.fromNbt(compound.getCompound("Container"));
        this.fluidToInsert = FluidVolume.fromTag(compound.getCompound("Fluid"));
    }

    public ItemStack getContainerStack() {
        return containerStack;
    }

    public FluidVolume getFluidToInsert() {
        return fluidToInsert;
    }

    public NbtCompound writeTag() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.put("Container", this.containerStack.writeNbt(new NbtCompound()));
        nbtCompound.put("Fluid", this.fluidToInsert.toTag());

        return nbtCompound;
    }
}
