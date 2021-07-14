package gregtech.api.worldgen.populator;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;

import java.util.Random;

public class SurfaceBlockPopulator extends OreVeinPopulator<SurfaceBlockPopulatorConfig> {

    public SurfaceBlockPopulator(Codec<SurfaceBlockPopulatorConfig> configCodec) {
        super(configCodec);
    }

    @Override
    boolean generate(Random random, ServerWorldAccess world, BlockPos origin, SurfaceBlockPopulatorConfig config) {
        int blocksCount = config.getBlockCount().get(random);
        int radius = config.getRadius().get(random);
        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        for (int i = 0; i < blocksCount; i++) {
            double randomAngle = random.nextFloat() * (Math.PI * 2);
            int resultBlockX = origin.getX() + (int) Math.round(Math.sin(randomAngle) * radius);
            int resultBlockZ = origin.getZ() + (int) Math.round(Math.cos(randomAngle) * radius);

            blockPos.set(resultBlockX, origin.getY(), resultBlockZ);

            int worldOceanFloorY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, resultBlockX, resultBlockZ);
            blockPos.setY(worldOceanFloorY + 1);

            BlockState existingBlockState = world.getBlockState(blockPos);
            if (existingBlockState.isIn(BlockTags.FEATURES_CANNOT_REPLACE)) {
                continue;
            }

            FluidState fluidState = existingBlockState.getFluidState();
            boolean isInsideOfTheWater = fluidState.isIn(FluidTags.WATER);

            if (isInsideOfTheWater && !config.shouldGenerateUnderwater()) {
                continue;
            }

            BlockState blockState = config.getBlockState();
            if (!world.setBlockState(blockPos, blockState, Block.NOTIFY_LISTENERS)) {
                continue;
            }

            if (isInsideOfTheWater && blockState.getBlock() instanceof Waterloggable waterloggable) {
                waterloggable.tryFillWithFluid(world, blockPos, blockState, Fluids.WATER.getDefaultState());
            }
        }
        return true;
    }

}
