package gregtech.api.items.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface AOEBreakItem {

    List<BlockPos> getAOEBlocks(ItemStack itemStack, PlayerEntity player, BlockHitResult rayTraceResult);
}
