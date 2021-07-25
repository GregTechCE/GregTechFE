package gregtech.api.capability.impl.energy.combined;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.item.DischargeMode;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.item.TransferLimit;
import gregtech.api.util.VoltageTier;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.List;

public class CombinedElectricItem implements ElectricItem {

    private final List<? extends ElectricItem> electricItems;

    public CombinedElectricItem(List<? extends ElectricItem> electricItems) {
        this.electricItems = electricItems;
    }

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
        return this.electricItems.stream().anyMatch(ElectricItem::canProvideChargeExternally);
    }

    @Override
    public long getMaxCharge() {
        return this.electricItems.stream()
                .mapToLong(ElectricItem::getMaxCharge)
                .sum();
    }

    @Override
    public long getCharge() {
        return this.electricItems.stream()
                .mapToLong(ElectricItem::getCharge)
                .sum();
    }

    @Override
    public VoltageTier getVoltageTier() {
        return this.electricItems.stream()
                .map(ElectricItem::getVoltageTier)
                .min(Comparator.comparing(VoltageTier::getVoltage))
                .orElse(VoltageTier.ULV);
    }

    @Override
    public boolean setMaxChargeOverride(long newMaxCharge, Simulation simulation) {
        if (this.electricItems.size() == 1) {
            return this.electricItems.get(0).setMaxChargeOverride(newMaxCharge, simulation);
        }
        //TODO simple for a single item, quite difficult to implement for multiplexed electric item
        return false;
    }

    @Override
    public void addItemTooltip(List<Text> tooltip, TooltipContext context) {
        for (ElectricItem electricItem : this.electricItems) {
            electricItem.addItemTooltip(tooltip, context);
        }
    }
}
