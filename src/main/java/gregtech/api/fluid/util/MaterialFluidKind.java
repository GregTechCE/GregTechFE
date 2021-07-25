package gregtech.api.fluid.util;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.material.properties.FluidProperties;

import java.util.Optional;

public enum MaterialFluidKind {
    FLUID;

    public Optional<FluidProperties> queryFluidProperties(Material material) {
        return material.queryProperty(MaterialFlags.FLUID_PROPERTIES);
    }

    public boolean shouldGenerateFor(Material material) {
        return true;
    }
}
