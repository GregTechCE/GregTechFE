package gregtech.api.capability.impl.energy.empty;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.capability.block.EnergySource;
import gregtech.api.util.VoltageTier;

public enum EmptyEnergySource implements EnergySource, NullVariant {
    INSTANCE;

    @Override
    public VoltageTier getVoltageTier() {
        return VoltageTier.ULV;
    }

    @Override
    public int pullEnergyIntoNetwork(int voltage, int amperage, Simulation simulation) {
        return 0;
    }
}
