package gregtech.api.capability.block;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.util.VoltageTier;

public interface EnergySink {

    /**
     * Retrieves maximum voltage supported by this energy sink without causing an over-voltage
     * Used for diagnostic purposes and to trace potential over-voltage issues in the network
     *
     * @return maximum voltage supported by this energy sink
     */
    VoltageTier getVoltageTier();

    /**
     * Called to accept energy from the network with the provided voltage and maximum amperage
     * @param voltage voltage in the network (may be lower than getVoltage() due to cable loss)
     * @param amperage maximum amperage available in the network
     * @return number of amperes accepted from the network
     */
    int acceptEnergyFromNetwork(int voltage, int amperage, Simulation simulation);
}
