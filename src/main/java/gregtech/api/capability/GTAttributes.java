package gregtech.api.capability;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.Attributes;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.block.ScannableBlock;
import gregtech.api.capability.item.ElectricItem;

public class GTAttributes {

    public static final Attribute<ElectricItem> ELECTRIC_ITEM = Attributes.create(ElectricItem.class);

    public static final Attribute<EnergyContainer> ENERGY_CONTAINER = Attributes.create(EnergyContainer.class);
}
