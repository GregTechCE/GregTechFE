package gregtech.api.capability.impl.energy;

import gregtech.api.capability.block.EnergySink;

import java.util.List;

public class CombinedEnergySink implements EnergySink {

    private final List<? extends EnergySink> sinks;

    public CombinedEnergySink(List<? extends EnergySink> sinks) {
        this.sinks = sinks;
    }

    @Override
    public int getVoltage() {
        return this.sinks.stream()
                .mapToInt(EnergySink::getVoltage)
                .min()
                .orElse(0);
    }

    @Override
    public int acceptEnergyFromNetwork(int voltage, int amperage) {
        int amperesAccepted = 0;

        for (EnergySink energySink : this.sinks) {
            int accepted = energySink.acceptEnergyFromNetwork(voltage, amperage);
            amperesAccepted += accepted;
            amperage -= accepted;
            if (amperage == 0) break;
        }
        return amperesAccepted;
    }
}
