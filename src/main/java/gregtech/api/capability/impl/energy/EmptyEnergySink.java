package gregtech.api.capability.impl.energy;

import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.capability.block.EnergySink;

public enum EmptyEnergySink implements EnergySink, NullVariant {
    INSTANCE;

    @Override
    public int getVoltage() {
        return 0;
    }

    @Override
    public int acceptEnergyFromNetwork(int voltage, int amperage) {
        return 0;
    }
}
