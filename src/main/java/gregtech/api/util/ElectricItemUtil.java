package gregtech.api.util;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.item.ElectricItem;

public class ElectricItemUtil {

    public static long chargeElectricItem(ElectricItem source, ElectricItem target, boolean externally, boolean ignoreTransferLimit) {
        long maxDischarged = source.discharge(Integer.MAX_VALUE, source.getTier(), ignoreTransferLimit, externally, Simulation.SIMULATE);
        long maxReceived = target.charge(maxDischarged, source.getTier(), ignoreTransferLimit, Simulation.SIMULATE);

        if(maxReceived > 0L) {
            long resultDischarged = source.discharge(maxReceived, source.getTier(), ignoreTransferLimit, externally, Simulation.ACTION);
            target.charge(resultDischarged, source.getTier(), ignoreTransferLimit, Simulation.ACTION);
            return resultDischarged;
        }
        return 0L;
    }
}
