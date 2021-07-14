package gregtech.api.worldgen.populator;

import gregtech.api.GTValues;
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
