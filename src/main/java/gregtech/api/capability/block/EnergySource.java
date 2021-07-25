package gregtech.api.capability.block;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.util.VoltageTier;

public interface EnergySource {

    /**
     * Retrieves maximum voltage supported by this energy source
     * {@link #pullEnergyIntoNetwork} will be usually called with the voltage received from this method,
     * although lower voltage values might also be used
     *
     * @return maximum voltage supported by this receiver
     */
    VoltageTier getVoltageTier();

    /**
     * Pulls energy from this energy source into the network
     * IMPORTANT! Simulated results should be consistent with real actions,
     * and source should be able to pull all amperage values lower than the returned one
     *
     * @param voltage voltage to pull
     * @param amperage maximum amperage that is allowed to be pulled from this energy source
     * @param simulation whenever action is simulated or not
     * @return amount of amperes available for network distribution
     */
    int pullEnergyIntoNetwork(int voltage, int amperage, Simulation simulation);
}
