package gregtech.api.multiblock.impl;

import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.PatternMatchContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

class MutableBlockWorldState implements BlockWorldState {

    private final World world;
    private final PatternMatchContext matchContext;
    private final PatternMatchContext layerContext;

    private BlockPos pos;
    private BlockState state;
    private BlockEntity blockEntity;
    private boolean blockEntityFetched;

    public MutableBlockWorldState(World world, PatternMatchContext matchContext, PatternMatchContext layerContext) {
        this.world = world;
        this.matchContext = matchContext;
        this.layerContext = layerContext;
    }

    public void update(BlockPos posIn) {
        this.pos = posIn;
        this.state = world.getBlockState(posIn);
        this.blockEntity = null;
        this.blockEntityFetched = false;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public BlockPos getPos() {
        return pos.toImmutable();
    }

    @Override
    public BlockState getBlockState() {
        return state;
    }

    @Nullable
    public BlockEntity getBlockEntity() {
        if (blockEntity == null && !blockEntityFetched) {
            this.blockEntity = world.getBlockEntity(pos);
            this.blockEntityFetched = true;
        }
        return blockEntity;
    }

    @Override
    public PatternMatchContext getMatchContext() {
        return matchContext;
    }

    @Override
    public PatternMatchContext getLayerContext() {
        return layerContext;
    }
}
