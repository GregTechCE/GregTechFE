package gregtech.api.items.stats;

import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Reference;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.impl.energy.SimpleElectricItem;
import gregtech.api.util.VoltageTier;
import net.minecraft.item.ItemStack;

public class ElectricStats {

    public static final ElectricStats EMPTY = new ElectricStats(0, VoltageTier.ULV, false, false);

    public final long maxCharge;
    public final VoltageTier tier;

    public final boolean canRecharge;
    public final boolean canProvideChargeExternally;

    public ElectricStats(long maxCharge, VoltageTier tier, boolean canRecharge, boolean canProvideChargeExternally) {
        this.maxCharge = maxCharge;
        this.tier = tier;
        this.canRecharge = canRecharge;
        this.canProvideChargeExternally = canProvideChargeExternally;
    }

    public ElectricItem createImplementation(Reference<ItemStack> stack, LimitedConsumer<ItemStack> excess) {
        return new SimpleElectricItem(stack, excess, maxCharge, tier, canRecharge, canProvideChargeExternally);
    }

    public static ElectricStats createElectricItem(long maxCharge, VoltageTier tier) {
        return new ElectricStats(maxCharge, tier, true, false);
    }

    public static ElectricStats createRechargeableBattery(long maxCharge, VoltageTier tier) {
        return new ElectricStats(maxCharge, tier, true, true);
    }

    public static ElectricStats createBattery(long maxCharge, VoltageTier tier, boolean rechargeable) {
        return new ElectricStats(maxCharge, tier, rechargeable, true);
    }
}
