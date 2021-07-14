package gregtech.api.worldgen.feature;

import gregtech.api.GTValues;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;

public class GTFeatures {

    public static final Feature<OreVeinFeatureConfig> ORE_VEIN;

    private static <T extends Feature<?>> T register(String name, T feature) {
        return Registry.register(Registry.FEATURE, new Identifier(GTValues.MODID, name), feature);
    }

    public static void ensureInitialized() {
    }

    static {
        ORE_VEIN = new OreVeinFeature(OreVeinFeatureConfig.CODEC);
    }
}
