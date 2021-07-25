package gregtech.api.recipe;

import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import net.minecraft.item.ItemStack;

public interface DynamicMachineRecipe {

    boolean canInputFluid(FluidKey fluidKey);

    boolean canInputItem(ItemStack itemStack);
}
