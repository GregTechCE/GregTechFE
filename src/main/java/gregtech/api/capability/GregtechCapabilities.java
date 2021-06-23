package gregtech.api.capability;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.Attributes;
import gregtech.api.capability.tool.IScrewdriverItem;
import gregtech.api.capability.tool.ISoftHammerItem;
import gregtech.api.capability.tool.IWrenchItem;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class GregtechCapabilities {

    public static final Attribute<IElectricItem> ELECTRIC_ITEM = Attributes.create(IElectricItem.class);

    @CapabilityInject(IEnergyContainer.class)
    public static Capability<IEnergyContainer> CAPABILITY_ENERGY_CONTAINER = null;

    @CapabilityInject(IWrenchItem.class)
    public static Capability<IWrenchItem> CAPABILITY_WRENCH = null;

    @CapabilityInject(IScrewdriverItem.class)
    public static Capability<IScrewdriverItem> CAPABILITY_SCREWDRIVER = null;

    @CapabilityInject(ISoftHammerItem.class)
    public static Capability<ISoftHammerItem> CAPABILITY_MALLET = null;

    @CapabilityInject(IFuelable.class)
    public static Capability<IFuelable> CAPABILITY_FUELABLE = null;

}
