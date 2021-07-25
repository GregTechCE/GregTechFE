package gregtech.api.capability;

import alexiil.mc.lib.attributes.Attributes;
import alexiil.mc.lib.attributes.CombinableAttribute;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.block.EnergySink;
import gregtech.api.capability.block.EnergySource;
import gregtech.api.capability.impl.energy.combined.CombinedElectricItem;
import gregtech.api.capability.impl.energy.combined.CombinedEnergyContainer;
import gregtech.api.capability.impl.energy.combined.CombinedEnergySink;
import gregtech.api.capability.impl.energy.combined.CombinedEnergySource;
import gregtech.api.capability.impl.energy.empty.EmptyElectricItem;
import gregtech.api.capability.impl.energy.empty.EmptyEnergyContainer;
import gregtech.api.capability.impl.energy.empty.EmptyEnergySink;
import gregtech.api.capability.impl.energy.empty.EmptyEnergySource;
import gregtech.api.capability.item.ElectricItem;

public class GTAttributes {

    public static final CombinableAttribute<ElectricItem> ELECTRIC_ITEM = Attributes.<ElectricItem>createCombinable(
            ElectricItem.class, EmptyElectricItem.INSTANCE, CombinedElectricItem::new);

    public static final CombinableAttribute<EnergyContainer> ENERGY_CONTAINER = Attributes.<EnergyContainer>createCombinable(
            EnergyContainer.class, EmptyEnergyContainer.INSTANCE, CombinedEnergyContainer::new);

    public static final CombinableAttribute<EnergySink> ENERGY_SINK = Attributes.<EnergySink>createCombinable(
            EnergySink.class, EmptyEnergySink.INSTANCE, CombinedEnergySink::new);

    public static final CombinableAttribute<EnergySource> ENERGY_SOURCE = Attributes.<EnergySource>createCombinable(
            EnergySource.class, EmptyEnergySource.INSTANCE, CombinedEnergySource::new);
}
