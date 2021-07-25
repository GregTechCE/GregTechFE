package gregtech.api.world.feature.vein;

import gregtech.api.GTValues;
import gregtech.api.world.feature.vein.populator.FluidSpringPopulator;
import gregtech.api.world.feature.vein.populator.FluidSpringPopulatorConfig;
import gregtech.api.world.feature.vein.populator.SurfaceBlockPopulator;
import gregtech.api.world.feature.vein.populator.SurfaceBlockPopulatorConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class OreVeinPopulators {

    public static final OreVeinPopulator<FluidSpringPopulatorConfig> FLUID_SPRING;
    public static final OreVeinPopulator<SurfaceBlockPopulatorConfig> SURFACE_BLOCK;

    private static <T extends OreVeinPopulator<?>> T register(String name, T populator) {
        return Registry.register(OreVeinPopulator.REGISTRY, new Identifier(GTValues.MODID, name), populator);
    }

    public static void ensureInitialized() {
    }

    static {
        FLUID_SPRING = register("fluid_spring", new FluidSpringPopulator(FluidSpringPopulatorConfig.CODEC));
        SURFACE_BLOCK = register("surface_block", new SurfaceBlockPopulator(SurfaceBlockPopulatorConfig.CODEC));
    }
}
