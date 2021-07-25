package gregtech.api.item.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AOEBreakItem {

    List<BlockPos> getAOEBlocks(ItemStack itemStack, PlayerEntity player, @Nullable HitResult rayTraceResult);
}
