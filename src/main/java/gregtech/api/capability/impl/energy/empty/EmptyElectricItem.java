package gregtech.api.capability.impl.energy.empty;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.capability.item.DischargeMode;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.item.TransferLimit;
import gregtech.api.util.VoltageTier;

public enum EmptyElectricItem implements ElectricItem, NullVariant {
    INSTANCE;


    @Override
    public long charge(long maxAmount, VoltageTier chargerTier, TransferLimit transferLimit, Simulation simulate) {
        return 0;
    }

    @Override
    public long discharge(long maxAmount, VoltageTier dischargerTier, TransferLimit transferLimit, DischargeMode mode, Simulation simulate) {
        return 0;
    }

    @Override
    public boolean canProvideChargeExternally() {
        return false;
    }

    @Override
    public long getMaxCharge() {
        return 0;
    }

    @Override
    public long getCharge() {
        return 0;
    }

    @Override
    public VoltageTier getVoltageTier() {
        return VoltageTier.ULV;
    }
}
