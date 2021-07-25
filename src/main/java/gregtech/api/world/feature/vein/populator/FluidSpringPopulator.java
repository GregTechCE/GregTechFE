package gregtech.api.world.feature.vein.populator;

import com.mojang.serialization.Codec;
import gregtech.api.util.ArrayUtils;
import gregtech.api.world.feature.vein.OreVeinPopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;

import java.util.Random;

public class FluidSpringPopulator extends OreVeinPopulator<FluidSpringPopulatorConfig> {

    public FluidSpringPopulator(Codec<FluidSpringPopulatorConfig> configCodec) {
        super(configCodec);
    }

    @Override
    protected boolean generate(Random random, ServerWorldAccess world, BlockPos origin, FluidSpringPopulatorConfig config) {
        int worldSurfaceY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, origin.getX(), origin.getZ());
        int springTopY = worldSurfaceY + config.getSpringHeight();

        BlockPos.Mutable tempPos = new BlockPos.Mutable();

        //place spring blocks right from the vein's origin
        for (int y = origin.getY(); y <= springTopY; y++) {
            placeSpringBlockAt(world, origin, config, tempPos, y, 0, 0);
            placeSpringBlockAt(world, origin, config, tempPos, y, 1, 0);
            placeSpringBlockAt(world, origin, config, tempPos, y, -1, 0);
            placeSpringBlockAt(world, origin, config, tempPos, y, 0, 1);
            placeSpringBlockAt(world, origin, config, tempPos, y, 0, -1);
        }
        placeSpringBlockAt(world, origin, config, tempPos, springTopY + 1, 0, 0);

        //generate oil cracks around the surface level of the spring
        for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
            Direction direction = Direction.fromHorizontal(directionIndex);
            continueCrackInDirection(random, world, origin, config, tempPos, worldSurfaceY, 0, 0, direction, 0);
        }

        return true;
    }

    private static void continueCrackInDirection(Random random, ServerWorldAccess world, BlockPos origin, FluidSpringPopulatorConfig config, BlockPos.Mutable tempPos, int y, int offsetX, int offsetZ, Direction direction, int crackNesting) {
        offsetX += direction.getOffsetX();
        offsetZ += direction.getOffsetZ();

        int squaredDistanceToOrigin = offsetX * offsetX + offsetZ * offsetZ;
        int squaredMaxDistance = config.getCracksRadius() * config.getCracksRadius();
        float normalizedDistance = squaredDistanceToOrigin / (squaredMaxDistance * 1.0f);

        //place the fluid blocks in the crack (double depth if distance is <= 0.7 to the origin and we're the first-level crack)
        placeSpringBlockAt(world, origin, config, tempPos, y, offsetX, offsetZ);
        if (normalizedDistance <= 0.7 && crackNesting <= 1) {
            placeSpringBlockAt(world, origin, config, tempPos, y - 1, offsetX, offsetZ);
        }

        //stop if we are exceeding the maximum radius of the crack
        if (normalizedDistance >= 1.0) {
            return;
        }
        //third-level cracks can never continue, they are single-block only
        if (crackNesting >= 2) {
            return;
        }

        //try to split up before going in the next direction
        float splitChance = crackNesting <= 0 ? 0.30f : 0.60f;
        if (random.nextFloat() <= splitChance) {
            boolean checkDistance = crackNesting <= 0;
            Direction splitDirection = getRandomDirection(random, offsetX, offsetZ, checkDistance);

            continueCrackInDirection(random, world, origin, config, tempPos, y, offsetX, offsetZ, splitDirection, crackNesting + 1);
        }

        //first level cracks have the lower chance of going in the same direction
        float sameDirectionChance = crackNesting <= 0 ? 0.50f : 0.70f;

        //determine final direction to continue the crack in
        Direction nextBlockDirection = direction;

        if (random.nextFloat() >= sameDirectionChance) {
            nextBlockDirection = getRandomDirection(random, offsetX, offsetZ, true);
        }

        continueCrackInDirection(random, world, origin, config, tempPos, y, offsetX, offsetZ, nextBlockDirection, crackNesting);
    }

    private static Direction getRandomDirection(Random random, int offsetX, int offsetZ, boolean checkDistance) {
        int currentSquaredDistance = offsetX * offsetX + offsetZ * offsetZ;

        int[] shuffledIndices = new int[] {0, 1, 2, 3};
        ArrayUtils.shuffle(random, shuffledIndices);

        for (int directionIndex : shuffledIndices) {
            Direction direction = Direction.fromHorizontal(directionIndex);

            int newOffsetX = offsetX + direction.getOffsetX();
            int newOffsetZ = offsetZ + direction.getOffsetZ();
            int newSquaredDistance = newOffsetX * newOffsetX + newOffsetZ * newOffsetZ;

            if (newSquaredDistance >= currentSquaredDistance || !checkDistance) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

    private static void placeSpringBlockAt(ServerWorldAccess world, BlockPos origin, FluidSpringPopulatorConfig config, BlockPos.Mutable tempPos, int y, int offsetX, int offsetZ) {
        tempPos.set(origin.getX() + offsetX, y, origin.getZ() + offsetZ);
        BlockState blockState = world.getBlockState(tempPos);
        if (!blockState.isIn(BlockTags.FEATURES_CANNOT_REPLACE)) {
            world.setBlockState(tempPos, config.getFluidState(), Block.NOTIFY_LISTENERS);
        }
    }

}
