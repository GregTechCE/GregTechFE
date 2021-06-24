package gregtech.api.capability.impl.fluid;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;

public class VoidFluidHandlerItem implements FluidInsertable {

    @Override
    public FluidVolume attemptInsertion(FluidVolume fluid, Simulation simulation) {
        return FluidVolumeUtil.EMPTY;
    }
}
