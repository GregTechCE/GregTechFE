package gregtech.api.capability.impl.energy.combined;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.block.EnergySink;
import gregtech.api.util.VoltageTier;

import java.util.Comparator;
import java.util.List;

public class CombinedEnergySink implements EnergySink {

    private final List<? extends EnergySink> sinks;

    public CombinedEnergySink(List<? extends EnergySink> sinks) {
        this.sinks = sinks;
    }

    @Override
    public VoltageTier getVoltageTier() {
        return this.sinks.stream()
                .map(EnergySink::getVoltageTier)
                .min(Comparator.comparing(VoltageTier::getVoltage))
                .orElse(VoltageTier.ULV);
    }

    @Override
    public int acceptEnergyFromNetwork(int voltage, int amperage, Simulation simulation) {
        int amperesAccepted = 0;

        for (EnergySink energySink : this.sinks) {
            int accepted = energySink.acceptEnergyFromNetwork(voltage, amperage, simulation);
            amperesAccepted += accepted;
            amperage -= accepted;
            if (amperage == 0) break;
        }
        return amperesAccepted;
    }
}
