package gregtech.api.util;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.block.EnergySink;
import gregtech.api.capability.block.EnergySource;

public class EnergyNetworkUtil {

    public static void transferEnergy(EnergySource source, EnergySink target) {
        transferEnergy(source, target, source.getVoltageTier().getVoltage(), Integer.MAX_VALUE);
    }

    public static void transferEnergy(EnergySource source, EnergySink target, int voltage, int maxAmperage) {
        int amperageExtracted = source.pullEnergyIntoNetwork(voltage, maxAmperage, Simulation.SIMULATE);
        int amperageReceived = target.acceptEnergyFromNetwork(voltage, amperageExtracted, Simulation.SIMULATE);

        if (amperageReceived > 0) {
            int actualAmperageExtracted = source.pullEnergyIntoNetwork(voltage, amperageReceived, Simulation.SIMULATE);
            int amperageActuallyReceived = target.acceptEnergyFromNetwork(voltage, actualAmperageExtracted, Simulation.SIMULATE);

            if (actualAmperageExtracted == amperageActuallyReceived) {
                source.pullEnergyIntoNetwork(voltage, amperageReceived, Simulation.ACTION);
                target.acceptEnergyFromNetwork(voltage, actualAmperageExtracted, Simulation.ACTION);
            }
        }
    }
}
