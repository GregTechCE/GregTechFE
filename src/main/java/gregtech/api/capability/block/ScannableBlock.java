package gregtech.api.capability.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;

public interface ScannableBlock {

    /**
     * Called when block is analyzed with a magnifying glass
     * Return complete description of the block here
     */
    List<Text> getMagnifyResults(BlockView world, BlockPos pos, BlockState blockState, PlayerEntity player);


    default int getScanDuration(BlockView world, BlockPos pos, BlockState blockState, PlayerEntity player) {
        return 60;
    }
}
