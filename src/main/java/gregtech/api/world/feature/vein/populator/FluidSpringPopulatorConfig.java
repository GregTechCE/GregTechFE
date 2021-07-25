package gregtech.api.world.feature.vein.populator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gregtech.api.world.feature.vein.OreVeinPopulatorConfig;
import net.minecraft.block.BlockState;

public class FluidSpringPopulatorConfig implements OreVeinPopulatorConfig {

    public static final Codec<FluidSpringPopulatorConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.intRange(1, 16).optionalFieldOf("spring_height", 8).forGetter(FluidSpringPopulatorConfig::getSpringHeight),
                    BlockState.CODEC.fieldOf("state").forGetter(FluidSpringPopulatorConfig::getFluidState),
                    Codec.intRange(0, 16).optionalFieldOf("cracks_radius", 7).forGetter(FluidSpringPopulatorConfig::getCracksRadius)
            ).apply(instance, FluidSpringPopulatorConfig::new));

    private final int springHeight;
    private final BlockState fluidState;
    private final int cracksRadius;

    public FluidSpringPopulatorConfig(int springHeight, BlockState fluidState, int cracksRadius) {
        this.springHeight = springHeight;
        this.fluidState = fluidState;
        this.cracksRadius = cracksRadius;
    }

    public int getSpringHeight() {
        return springHeight;
    }

    public BlockState getFluidState() {
        return fluidState;
    }

    public int getCracksRadius() {
        return cracksRadius;
    }
}
