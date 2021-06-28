package gregtech.api.items.stats;

import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Reference;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.impl.ElectricItemImpl;
import net.minecraft.item.ItemStack;

public class ElectricStats {

    public static final ElectricStats EMPTY = new ElectricStats(0, 0, false, false);

    public final long maxCharge;
    public final int tier;

    public final boolean canRecharge;
    public final boolean canProvideChargeExternally;

    public ElectricStats(long maxCharge, long tier, boolean canRecharge, boolean canProvideChargeExternally) {
        this.maxCharge = maxCharge;
        this.tier = (int) tier;
        this.canRecharge = canRecharge;
        this.canProvideChargeExternally = canProvideChargeExternally;
    }

    public ElectricItem createImplementation(Reference<ItemStack> stack, LimitedConsumer<ItemStack> excess) {
        return new ElectricItemImpl(stack, excess, maxCharge, tier, canRecharge, canProvideChargeExternally);
    }

    public static ElectricStats createElectricItem(long maxCharge, long tier) {
        return new ElectricStats(maxCharge, tier, true, false);
    }

    public static ElectricStats createRechargeableBattery(long maxCharge, int tier) {
        return new ElectricStats(maxCharge, tier, true, true);
    }

    public static ElectricStats createBattery(long maxCharge, int tier, boolean rechargeable) {
        return new ElectricStats(maxCharge, tier, rechargeable, true);
    }
}
