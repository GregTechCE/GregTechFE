package gregtech.api.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface BlockWorldState {

    World getWorld();

    BlockPos getPos();

    BlockState getBlockState();

    @Nullable BlockEntity getBlockEntity();

    PatternMatchContext getMatchContext();

    PatternMatchContext getLayerContext();
}
