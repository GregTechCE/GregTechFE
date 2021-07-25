package gregtech.api.fluid.render;

import gregtech.api.GTValues;
import net.minecraft.util.Identifier;

public class MaterialFluidTexture {

    public static final MaterialFluidTexture DEFAULT_FLUID = MaterialFluidTexture.create(
            new Identifier(GTValues.MODID, "block/fluid/default_fluid_flowing"),
            new Identifier(GTValues.MODID, "block/fluid/default_fluid_still"),
            true
    );

    public static final MaterialFluidTexture DEFAULT_GAS = MaterialFluidTexture.create(
            new Identifier(GTValues.MODID, "block/fluid/default_gas_flowing"),
            new Identifier(GTValues.MODID, "block/fluid/default_gas_still"),
            true
    );

    public static final MaterialFluidTexture MOLTEN_FLUID = MaterialFluidTexture.create(
            new Identifier(GTValues.MODID, "block/fluid/molten_fluid_flowing"),
            new Identifier(GTValues.MODID, "block/fluid/molten_fluid_still"),
            true
    );

    private final Identifier flowingTexture;
    private final Identifier stillTexture;
    private final boolean tintFluidSprite;

    private MaterialFluidTexture(Identifier flowingTexture, Identifier stillTexture, boolean tintFluidSprite) {
        this.flowingTexture = flowingTexture;
        this.stillTexture = stillTexture;
        this.tintFluidSprite = tintFluidSprite;
    }

    public static MaterialFluidTexture create(Identifier flowingTexture, Identifier stillTexture, boolean tintFluidSprite) {
        return new MaterialFluidTexture(flowingTexture, stillTexture, tintFluidSprite);
    }

    public Identifier getFlowingTexture() {
        return flowingTexture;
    }

    public Identifier getStillTexture() {
        return stillTexture;
    }

    public boolean shouldTintFluidSprite() {
        return tintFluidSprite;
    }
}
