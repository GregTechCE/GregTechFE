package gregtech.api.util;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.item.DischargeMode;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.item.TransferLimit;

public class ElectricItemUtil {

    public static long chargeElectricItem(ElectricItem source, ElectricItem target, DischargeMode dischargeMode, TransferLimit transferLimit) {
        VoltageTier tier = source.getVoltageTier();
        long maxItemDischarge = source.discharge(Integer.MAX_VALUE, tier, transferLimit, dischargeMode, Simulation.SIMULATE);
        long energyCanBeAdded = target.charge(maxItemDischarge, tier, transferLimit, Simulation.SIMULATE);

        long energyExtracted = source.discharge(energyCanBeAdded, tier, transferLimit, dischargeMode, Simulation.SIMULATE);
        long energyActuallyAdded = target.charge(energyExtracted, tier, transferLimit, Simulation.SIMULATE);

        if (energyActuallyAdded == energyExtracted) {
            source.discharge(energyCanBeAdded, tier, transferLimit, dischargeMode, Simulation.ACTION);
            target.charge(energyExtracted, tier, transferLimit, Simulation.ACTION);

            return energyActuallyAdded;
        }
        return 0L;
    }

    public static void dischargeElectricItem(ElectricItem source, EnergyContainer target, TransferLimit transferLimit, DischargeMode dischargeMode) {
        VoltageTier tier = target.getVoltageTier();
        long maxItemDischarge = source.discharge(Integer.MAX_VALUE, tier, transferLimit, dischargeMode, Simulation.SIMULATE);
        long energyCanBeAdded = target.addEnergy(maxItemDischarge, Simulation.SIMULATE);

        long energyExtracted = source.discharge(energyCanBeAdded, tier, transferLimit, dischargeMode, Simulation.SIMULATE);
        long energyActuallyAdded = target.addEnergy(energyExtracted, Simulation.SIMULATE);

        if (energyActuallyAdded == energyExtracted) {
            source.discharge(energyCanBeAdded, tier, transferLimit, dischargeMode, Simulation.ACTION);
            target.addEnergy(energyExtracted, Simulation.ACTION);
        }
    }

    public static void chargeElectricItem(EnergyContainer source, ElectricItem target, TransferLimit transferLimit) {
        VoltageTier tier = source.getVoltageTier();
        long maxContainerDischarge = source.removeEnergy(Integer.MAX_VALUE, Simulation.SIMULATE);
        long energyCanBeAdded = target.charge(maxContainerDischarge, tier, transferLimit, Simulation.SIMULATE);

        long energyExtracted = source.removeEnergy(energyCanBeAdded, Simulation.SIMULATE);
        long energyActuallyAdded = target.charge(energyExtracted, tier, transferLimit, Simulation.SIMULATE);

        if (energyActuallyAdded == energyExtracted) {
            source.removeEnergy(energyCanBeAdded, Simulation.ACTION);
            target.charge(energyExtracted, tier, transferLimit, Simulation.ACTION);
        }
    }
}
