package gregtech.api.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class OreVeinFeature extends Feature<OreVeinFeatureConfig> {

    public OreVeinFeature(Codec<OreVeinFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Nullable
    private static BlockState getMatchingTarget(Random random, OreVeinFeatureConfig.TargetGroup targetGroup, BlockState existingState) {
        for (OreVeinFeatureConfig.Target target : targetGroup.getTargets()) {
            if (target.getTarget().test(existingState, random)) {
                return target.getState();
            }
        }
        return null;
    }

    @Nullable
    private static OreVeinFeatureConfig.GenerationLayer getLayerForProximity(OreVeinFeatureConfig config, float proximityToTheBorder) {
        for (OreVeinFeatureConfig.GenerationLayer layer : config.getLayers()) {
            if (proximityToTheBorder >= layer.getOffset() &&
                    (proximityToTheBorder - layer.getOffset()) <= layer.getSize()) {
                return layer;
            }
        }
        return null;
    }

    @Nullable
    private BlockState getStateForGeneration(Random random, OreVeinFeatureConfig config, BlockState existingState, float proximityToTheBorder) {
        OreVeinFeatureConfig.GenerationLayer layer = getLayerForProximity(config, proximityToTheBorder);
        if (layer == null) {
            return null;
        }

        if (random.nextFloat() > layer.getDensity()) {
            return null;
        }

        float localLayerDistance = (proximityToTheBorder - layer.getOffset()) / layer.getSize();
        if (localLayerDistance >= layer.getFalloffDistance()) {
            float falloffPercent = (localLayerDistance - layer.getFalloffDistance()) / (1.0f - layer.getFalloffDistance());
            float actualFalloffDensity = layer.getFalloffDensity() * falloffPercent;

            if (random.nextFloat() <= actualFalloffDensity) {
                return null;
            }
        }

        Optional<OreVeinFeatureConfig.TargetGroup> targetGroup = layer.getTargetGroups().getDataOrEmpty(random);
        if (targetGroup.isEmpty()) {
            return null;
        }
        return getMatchingTarget(random, targetGroup.get(), existingState);
    }

    private void applyVeinDecorator(FeatureContext<OreVeinFeatureConfig> context) {
        context.getConfig().getVeinPopulator().generate(context.getRandom(), context.getWorld(), context.getOrigin());
    }

    @Override
    public boolean generate(FeatureContext<OreVeinFeatureConfig> context) {
        OreVeinFeatureConfig config = context.getConfig();
        BlockPos originPos = context.getOrigin();

        //Abort early if our origin is above world's height, this way we will never generate anything anyway
        int originWorldHeight = context.getWorld().getTopY(Heightmap.Type.OCEAN_FLOOR_WG, originPos.getX(), originPos.getZ());
        if (originPos.getY() > originWorldHeight) {
            return false;
        }

        Random random = context.getRandom();
        float yRotationDegrees = random.nextFloat() * 90.0f;
        float xRotationDegrees = random.nextFloat() * 360.0f;

        Quaternion quat = Vec3f.POSITIVE_Y.getDegreesQuaternion(yRotationDegrees);
        quat.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(xRotationDegrees));
        Matrix3f invertedRotationMatrix = new Matrix3f(quat);
        invertedRotationMatrix.invert();

        int xRadius = config.getFirstHorizontalSize().get(random) / 2;
        int zRadius = config.getSecondHorizontalSize().get(random) / 2;
        int yRadius = config.getVerticalSize().get(random) / 2;

        int maxRadius = Math.max(yRadius, Math.max(xRadius, zRadius));

        Vec3f resultPosVector = new Vec3f();
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        int oreBlocksGenerated = 0;

        for (int x = -maxRadius; x <= maxRadius; x++) {
            for (int z = -maxRadius; z <= maxRadius; z++) {
                for (int y = -maxRadius; y <= maxRadius; y++) {

                    resultPosVector.set(x + 0.5f, y + 0.5f, z + 0.5f);
                    resultPosVector.transform(invertedRotationMatrix);

                    float xComponent = (resultPosVector.getX() * resultPosVector.getX()) / (xRadius * xRadius);
                    float yComponent = (resultPosVector.getY() * resultPosVector.getY()) / (yRadius * yRadius);
                    float zComponent = (resultPosVector.getZ() * resultPosVector.getZ()) / (zRadius * zRadius);

                    float proximityToTheBorder = (xComponent + yComponent + zComponent);
                    if (proximityToTheBorder > 1.0f) continue;

                    mutableBlockPos.set(originPos.getX() + x, originPos.getY() + y, originPos.getZ() + z);

                    //Do not generate anything above world's surface
                    int blockPosTopY = context.getWorld().getTopY(Heightmap.Type.OCEAN_FLOOR_WG, mutableBlockPos.getX(), mutableBlockPos.getZ());
                    if (mutableBlockPos.getY() > blockPosTopY) continue;

                    BlockState existingState = context.getWorld().getBlockState(mutableBlockPos);

                    BlockState blockState = getStateForGeneration(random, config, existingState, proximityToTheBorder);
                    if (blockState != null) {
                        context.getWorld().setBlockState(mutableBlockPos, blockState, Block.NOTIFY_LISTENERS);
                        oreBlocksGenerated++;
                    }
                }
            }
        }

        if (oreBlocksGenerated >= config.getMinBlocksForPopulator()) {
            if (random.nextFloat() <= config.getPopulationChance()) {
                applyVeinDecorator(context);
            }
        }

        return oreBlocksGenerated > 0;
    }
}
