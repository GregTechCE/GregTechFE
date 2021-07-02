package gregtech.api.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface BlockWorldState {

    public World getWorld();

    public BlockPos getPos();

    public BlockState getBlockState();

    @Nullable
    public BlockEntity getBlockEntity();

    public PatternMatchContext getMatchContext();

    public PatternMatchContext getLayerContext();
}
