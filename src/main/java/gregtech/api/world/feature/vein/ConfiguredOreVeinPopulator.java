package gregtech.api.world.feature.vein;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

import java.util.Random;

public class ConfiguredOreVeinPopulator<C extends OreVeinPopulatorConfig> {

    public static final Codec<ConfiguredOreVeinPopulator<?>> CODEC = OreVeinPopulator.REGISTRY.dispatch(
            ConfiguredOreVeinPopulator::getVeinPopulator,
            OreVeinPopulator::getCodec);

    private final OreVeinPopulator<C> veinPopulator;
    private final C config;

    public ConfiguredOreVeinPopulator(OreVeinPopulator<C> veinPopulator, C config) {
        this.veinPopulator = veinPopulator;
        this.config = config;
    }

    public OreVeinPopulator<C> getVeinPopulator() {
        return veinPopulator;
    }

    public C getConfig() {
        return config;
    }

    public boolean generate(Random random, ServerWorldAccess world, BlockPos origin) {
        return this.veinPopulator.generate(random, world, origin, this.config);
    }
}
