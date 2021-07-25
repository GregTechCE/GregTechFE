package gregtech.api.world.feature.vein.populator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gregtech.api.world.feature.vein.OreVeinPopulatorConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.intprovider.IntProvider;

public class SurfaceBlockPopulatorConfig implements OreVeinPopulatorConfig {

    public static final Codec<SurfaceBlockPopulatorConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    BlockState.CODEC.fieldOf("block").forGetter(SurfaceBlockPopulatorConfig::getBlockState),
                    IntProvider.createValidatingCodec(0, 100).fieldOf("count").forGetter(SurfaceBlockPopulatorConfig::getBlockCount),
                    Codec.BOOL.optionalFieldOf("generate_underwater", true).forGetter(SurfaceBlockPopulatorConfig::shouldGenerateUnderwater),
                    IntProvider.createValidatingCodec(1, 32).fieldOf("radius").forGetter(SurfaceBlockPopulatorConfig::getRadius)
            ).apply(instance, SurfaceBlockPopulatorConfig::new));

    private final BlockState blockState;
    private final IntProvider blockCount;
    private final boolean generateUnderwater;
    private final IntProvider radius;

    public SurfaceBlockPopulatorConfig(BlockState blockState, IntProvider blockCount, boolean generateUnderwater, IntProvider radius) {
        this.blockState = blockState;
        this.blockCount = blockCount;
        this.generateUnderwater = generateUnderwater;
        this.radius = radius;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public IntProvider getBlockCount() {
        return blockCount;
    }

    public boolean shouldGenerateUnderwater() {
        return generateUnderwater;
    }

    public IntProvider getRadius() {
        return radius;
    }
}
