package gregtech.api.capability.impl.energy;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.block.EnergySource;

import java.util.List;

public class CombinedEnergySource implements EnergySource {

    private final List<? extends EnergySource> sources;

    public CombinedEnergySource(List<? extends EnergySource> sources) {
        this.sources = sources;
    }

    @Override
    public int getVoltage() {
        return this.sources.stream()
                .mapToInt(EnergySource::getVoltage)
                .min().orElse(0);
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
