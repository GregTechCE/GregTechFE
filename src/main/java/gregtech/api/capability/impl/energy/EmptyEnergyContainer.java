package gregtech.api.capability.impl.energy;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.block.EnergyContainerChangeListener;
import gregtech.api.util.VoltageTier;

public enum EmptyEnergyContainer implements EnergyContainer, NullVariant {
    INSTANCE;

    @Override
    public long addEnergy(long energyToAdd, Simulation simulate) {
        return 0;
    }

    @Override
    public long removeEnergy(long energyToRemove, Simulation simulate) {
        return 0;
    }

    @Override
    public long getEnergyStored() {
        return 0;
    }

    @Override
    public long getEnergyCapacity() {
        return 0;
    }

    @Override
    public VoltageTier getVoltageTier() {
        return VoltageTier.ULV;
    }

    @Override
    public ListenerToken addListener(EnergyContainerChangeListener listener, ListenerRemovalToken removalToken) {
        return null;
    }
}
