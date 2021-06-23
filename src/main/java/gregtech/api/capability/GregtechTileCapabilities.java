package gregtech.api.capability;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.Attributes;
import gregtech.api.cover.Coverable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class GregtechTileCapabilities {

    public static Attribute<Coverable> COVERABLE = Attributes.create(Coverable.class);

    @CapabilityInject(IWorkable.class)
    public static Capability<IWorkable> CAPABILITY_WORKABLE = null;

    @CapabilityInject(IControllable.class)
    public static Capability<IControllable> CAPABILITY_CONTROLLABLE = null;

    @CapabilityInject(IActiveOutputSide.class)
    public static Capability<IActiveOutputSide> CAPABILITY_ACTIVE_OUTPUT_SIDE = null;

}
