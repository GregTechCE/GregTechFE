package gregtech.api.worldgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gregtech.api.worldgen.populator.ConfiguredOreVeinPopulator;
import net.minecraft.block.BlockState;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.List;

public class OreVeinFeatureConfig implements FeatureConfig {

    private static final int DEFAULT_MIN_BLOCKS_FOR_POPULATOR = 100;

    public static final Codec<OreVeinFeatureConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    IntProvider.createValidatingCodec(1, 32).fieldOf("first_horizontal_size").forGetter(OreVeinFeatureConfig::getFirstHorizontalSize),
                    IntProvider.createValidatingCodec(1, 32).fieldOf("second_horizontal_size").forGetter(OreVeinFeatureConfig::getSecondHorizontalSize),
                    IntProvider.createValidatingCodec(1, 32).fieldOf("vertical_size").forGetter(OreVeinFeatureConfig::getVerticalSize),
                    GenerationLayer.CODEC.listOf().fieldOf("layers").forGetter(OreVeinFeatureConfig::getLayers),
                    ConfiguredOreVeinPopulator.CODEC.fieldOf("vein_populator").forGetter(OreVeinFeatureConfig::getVeinPopulator),
                    Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("min_blocks_for_populator", DEFAULT_MIN_BLOCKS_FOR_POPULATOR).forGetter(OreVeinFeatureConfig::getMinBlocksForPopulator),
                    Codec.floatRange(0.0f, 1.0f).optionalFieldOf("population_chance", 1.0f).forGetter(OreVeinFeatureConfig::getPopulationChance)
            ).apply(instance, OreVeinFeatureConfig::new));

    private final IntProvider firstHorizontalSize;
    private final IntProvider secondHorizontalSize;
    private final IntProvider verticalSize;
    private final List<GenerationLayer> layers;
    private final ConfiguredOreVeinPopulator<?> veinPopulator;
    private final int minBlocksForPopulator;
    private final float populationChance;

    public OreVeinFeatureConfig(IntProvider firstHorizontalSize, IntProvider secondHorizontalSize, IntProvider verticalSize, List<GenerationLayer> layers, ConfiguredOreVeinPopulator<?> veinPopulator, int minBlocksForPopulator, float populationChance) {
        this.firstHorizontalSize = firstHorizontalSize;
        this.secondHorizontalSize = secondHorizontalSize;
        this.verticalSize = verticalSize;
        this.layers = layers;
        this.veinPopulator = veinPopulator;
        this.minBlocksForPopulator = minBlocksForPopulator;
        this.populationChance = populationChance;
    }

    public IntProvider getFirstHorizontalSize() {
        return firstHorizontalSize;
    }

    public IntProvider getSecondHorizontalSize() {
        return secondHorizontalSize;
    }

    public IntProvider getVerticalSize() {
        return verticalSize;
    }

    public List<GenerationLayer> getLayers() {
        return layers;
    }

    public ConfiguredOreVeinPopulator<?> getVeinPopulator() {
        return veinPopulator;
    }

    public int getMinBlocksForPopulator() {
        return minBlocksForPopulator;
    }

    public float getPopulationChance() {
        return populationChance;
    }

    public static class GenerationLayer {

        public static final Codec<GenerationLayer> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(
                        DataPool.createCodec(TargetGroup.CODEC).fieldOf("target_groups").forGetter(GenerationLayer::getTargetGroups),
                        Codec.floatRange(0.0f, 1.0f).optionalFieldOf("offset", 0.0f).forGetter(GenerationLayer::getOffset),
                        Codec.floatRange(0.0f, 1.0f).optionalFieldOf("size", 1.0f).forGetter(GenerationLayer::getSize),

                        Codec.floatRange(0.0f, 1.0f).optionalFieldOf("density", 1.0f).forGetter(GenerationLayer::getDensity),
                        Codec.floatRange(0.0f, 1.0f).optionalFieldOf("falloff_distance", 1.0f).forGetter(GenerationLayer::getFalloffDistance),
                        Codec.floatRange(0.0f, 1.0f).optionalFieldOf("falloff_density", 1.0f).forGetter(GenerationLayer::getFalloffDensity)
                ).apply(instance, GenerationLayer::new));

        private final DataPool<TargetGroup> targetGroups;
        private final float offset;
        private final float size;

        private final float density;
        private final float falloffDistance;
        private final float falloffDensity;

        public GenerationLayer(DataPool<TargetGroup> targetGroups, float offset, float size, float density, float falloffDistance, float falloffDensity) {
            this.targetGroups = targetGroups;
            this.offset = offset;
            this.size = size;
            this.density = density;
            this.falloffDistance = falloffDistance;
            this.falloffDensity = falloffDensity;
        }

        public DataPool<TargetGroup> getTargetGroups() {
            return targetGroups;
        }

        public float getOffset() {
            return offset;
        }

        public float getSize() {
            return size;
        }

        public float getDensity() {
            return density;
        }

        public float getFalloffDistance() {
            return falloffDistance;
        }

        public float getFalloffDensity() {
            return falloffDensity;
        }
    }

    public static class TargetGroup {
        public static final Codec<TargetGroup> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(
                        Target.CODEC.listOf().fieldOf("targets").forGetter(TargetGroup::getTargets)
                ).apply(instance, TargetGroup::new));

        private final List<Target> targets;

        public TargetGroup(List<Target> targets) {
            this.targets = ImmutableList.copyOf(targets);
        }

        public List<Target> getTargets() {
            return targets;
        }
    }

    public static class Target {
        public static final Codec<Target> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(
                        RuleTest.TYPE_CODEC.fieldOf("target").forGetter(Target::getTarget),
                        BlockState.CODEC.fieldOf("state").forGetter(Target::getState)
                ).apply(instance, Target::new));

        private final RuleTest target;
        private final BlockState state;

        public Target(RuleTest ruleTest, BlockState blockState) {
            this.target = ruleTest;
            this.state = blockState;
        }

        public RuleTest getTarget() {
            return target;
        }

        public BlockState getState() {
            return state;
        }
    }
}
