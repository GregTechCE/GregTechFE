package gregtech.api.capability.block;

import alexiil.mc.lib.attributes.Simulation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public interface WrenchableBlock {

    boolean attemptWrench(BlockView world, BlockPos pos, BlockState state, @Nullable PlayerEntity player, Direction wrenchSide, Simulation simulation);
}
