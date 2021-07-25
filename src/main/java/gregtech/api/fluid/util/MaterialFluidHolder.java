package gregtech.api.fluid.util;

import gregtech.api.fluid.MaterialFluid;
import net.minecraft.block.FluidBlock;

public final class MaterialFluidHolder {
    private MaterialFluid still;
    private MaterialFluid flowing;
    private FluidBlock block;

    public MaterialFluid getStill() {
        return still;
    }

    public void setStill(MaterialFluid still) {
        this.still = still;
    }

    public MaterialFluid getFlowing() {
        return flowing;
    }

    public void setFlowing(MaterialFluid flowing) {
        this.flowing = flowing;
    }

    public FluidBlock getBlock() {
        return block;
    }

    public void setBlock(FluidBlock block) {
        this.block = block;
    }
}
