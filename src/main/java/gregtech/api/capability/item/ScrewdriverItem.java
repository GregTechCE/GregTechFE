package gregtech.api.capability.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ScrewdriverItem {

    default boolean canUseScrewdriver(ItemStack stack) { return true; }

    default void onBlockScrewdrivered(World world, ItemStack stack, PlayerEntity player, Hand hand, BlockPos blockPos) {}
}
