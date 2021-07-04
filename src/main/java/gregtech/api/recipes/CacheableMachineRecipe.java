package gregtech.api.recipes;

import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import net.minecraft.item.Item;

import java.util.List;

public interface CacheableMachineRecipe {

    boolean canBeCached();

    List<Item> getReferencedItems();

    List<FluidKey> getReferencedFluids();
}
