package gregtech.api.capability.impl.energy.empty;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.capability.block.EnergySink;
import gregtech.api.util.VoltageTier;

public enum EmptyEnergySink implements EnergySink, NullVariant {
    INSTANCE;

    @Override
    public VoltageTier getVoltageTier() {
        return VoltageTier.ULV;
    }

    @Override
    public int acceptEnergyFromNetwork(int voltage, int amperage, Simulation simulation) {
        return 0;
    }
}
