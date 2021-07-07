package gregtech.api.unification.material.properties;

import gregtech.api.fluids.MaterialFluidProperties;
import gregtech.api.fluids.MaterialFluidTexture;

public class FluidProperties {

    private final MaterialFluidProperties properties;
    private final MaterialFluidTexture texture;

    public FluidProperties(MaterialFluidProperties properties, MaterialFluidTexture texture) {
        this.properties = properties;
        this.texture = texture;
    }

    public static FluidProperties create(MaterialFluidProperties properties, MaterialFluidTexture texture) {
        return new FluidProperties(properties, texture);
    }

    public MaterialFluidProperties getProperties() {
        return properties;
    }

    public MaterialFluidTexture getTexture() {
        return texture;
    }
}
