package gregtech.api.capability.impl.energy;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.capability.block.EnergySource;

public enum EmptyEnergySource implements EnergySource, NullVariant {
    INSTANCE;

    @Override
    public int getVoltage() {
        return 0;
    }

    @Override
    public int pullEnergyIntoNetwork(int voltage, int amperage, Simulation simulation) {
        return 0;
    }
}
