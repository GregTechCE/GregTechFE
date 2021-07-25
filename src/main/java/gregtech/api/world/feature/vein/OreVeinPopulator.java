package gregtech.api.world.feature.vein;

import com.mojang.serialization.Codec;
import gregtech.api.GTValues;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ServerWorldAccess;

import java.util.Random;

public abstract class OreVeinPopulator<C extends OreVeinPopulatorConfig> {

    @SuppressWarnings("unchecked")
    public static final Registry<OreVeinPopulator<?>> REGISTRY = FabricRegistryBuilder
            .createSimple((Class<OreVeinPopulator<?>>) (Object) OreVeinPopulator.class, new Identifier(GTValues.MODID, "vein_populators"))
            .buildAndRegister();

    private final Codec<ConfiguredOreVeinPopulator<C>> codec;

    public OreVeinPopulator(Codec<C> configCodec) {
        this.codec = configCodec.fieldOf("config").xmap(
                (config) -> new ConfiguredOreVeinPopulator<>(this, config),
                ConfiguredOreVeinPopulator::getConfig)
                .codec();
    }

    public Codec<ConfiguredOreVeinPopulator<C>> getCodec() {
        return codec;
    }

    public ConfiguredOreVeinPopulator<?> configure(C config) {
        return new ConfiguredOreVeinPopulator<>(this, config);
    }

    abstract boolean generate(Random random, ServerWorldAccess world, BlockPos origin, C config);

    static {
        OreVeinPopulators.ensureInitialized();
    }
}
