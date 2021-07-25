package gregtech.api.capability.impl.energy.combined;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.block.EnergySource;
import gregtech.api.util.VoltageTier;

import java.util.Comparator;
import java.util.List;

public class CombinedEnergySource implements EnergySource {

    private final List<? extends EnergySource> sources;

    public CombinedEnergySource(List<? extends EnergySource> sources) {
        this.sources = sources;
    }

    @Override
    public VoltageTier getVoltageTier() {
        return this.sources.stream()
                .map(EnergySource::getVoltageTier)
                .min(Comparator.comparing(VoltageTier::getVoltage))
                .orElse(VoltageTier.ULV);
    }

    @Override
    public int pullEnergyIntoNetwork(int voltage, int amperage, Simulation simulation) {
        int amperagePulled = 0;

        for (EnergySource energySource : this.sources) {
            int pulled = energySource.pullEnergyIntoNetwork(voltage, amperage, simulation);
            amperagePulled += pulled;
            amperage -= pulled;
            if (amperage == 0) break;
        }
        return amperagePulled;
    }
}
