package gregtech.api.capability.block;

public interface EnergySink {

    /**
     * Retrieves maximum voltage supported by this energy sink without causing an over-voltage
     * Used for diagnostic purposes and to trace potential over-voltage issues in the network
     *
     * @return maximum voltage supported by this energy sink
     */
    int getVoltage();

    /**
     * Called to accept energy from the network with the provided voltage and maximum amperage
     * @param voltage voltage in the network
     * @param amperage maximum amperage available in the network
     * @return number of amperes accepted from the network
     */
    int acceptEnergyFromNetwork(int voltage, int amperage);
}
