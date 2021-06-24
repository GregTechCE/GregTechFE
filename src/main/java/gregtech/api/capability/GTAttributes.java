package gregtech.api.capability;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.Attributes;
import gregtech.api.capability.tool.ScrewdriverItem;
import gregtech.api.capability.tool.SoftHammerItem;
import gregtech.api.capability.tool.WrenchItem;

public class GTAttributes {

    public static final Attribute<ElectricItem> ELECTRIC_ITEM = Attributes.create(ElectricItem.class);

    public static final Attribute<EnergyContainer> ENERGY_CONTAINER = Attributes.create(EnergyContainer.class);

    public static final Attribute<WrenchItem> WRENCH_ITEM = Attributes.create(WrenchItem.class);

    public static final Attribute<ScrewdriverItem> SCREWDRIVER_ITEM = Attributes.create(ScrewdriverItem.class);

    public static final Attribute<SoftHammerItem> SOFT_HAMMER_ITEM = Attributes.create(SoftHammerItem.class);
}
