package gregtech.api.item;

import gregtech.api.item.stats.ElectricStats;
import gregtech.api.item.stats.FluidStats;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class GTItemSettings extends FabricItemSettings {

    FluidStats fluidStats;
    ElectricStats electricStats;

    public GTItemSettings() {
        this.group(GTItemGroups.MAIN);
    }

    public GTItemSettings fluidStats(FluidStats fluidStats) {
        this.fluidStats = fluidStats;
        return this;
    }

    public GTItemSettings electricStats(ElectricStats electricStats) {
        this.electricStats = electricStats;
        return this;
    }
}
