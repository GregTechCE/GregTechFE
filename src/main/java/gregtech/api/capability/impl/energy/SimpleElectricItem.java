package gregtech.api.capability.impl.energy;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.AbstractItemBasedAttribute;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Reference;
import gregtech.api.capability.item.DischargeMode;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.item.TransferLimit;
import gregtech.api.util.VoltageTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class SimpleElectricItem extends AbstractItemBasedAttribute implements ElectricItem {

    private static final String SUBTAG_NAME = "GT.ElectricItem";
    protected final long maxCharge;
    protected final VoltageTier voltageTier;

    protected final boolean rechargable;
    protected final boolean canProvideEnergyExternally;

    public SimpleElectricItem(Reference<ItemStack> stackRef, LimitedConsumer<ItemStack> excessStacks, long maxCharge, VoltageTier voltageTier, boolean rechargable, boolean canProvideEnergyExternally) {
        super(stackRef, excessStacks);
        this.maxCharge = maxCharge;
        this.voltageTier = voltageTier;
        this.rechargable = rechargable;
        this.canProvideEnergyExternally = canProvideEnergyExternally;
    }

    @Override
    public VoltageTier getVoltageTier() {
        return this.voltageTier;
    }

    @Override
    public boolean canProvideChargeExternally() {
        return this.canProvideEnergyExternally;
    }

    @Override
    public long getMaxCharge() {
        NbtCompound electricItemTag = this.stackRef.get().getSubTag(SUBTAG_NAME);

        if (electricItemTag != null && electricItemTag.contains("MaxCharge", NbtElement.NUMBER_TYPE)) {
            return electricItemTag.getLong("MaxCharge");
        }
        return this.maxCharge;
    }

    @Override
    public long getCharge() {
        NbtCompound electricItemTag = this.stackRef.get().getSubTag(SUBTAG_NAME);

        if (electricItemTag != null) {
            if (electricItemTag.getBoolean("Infinite")) {
                return getMaxCharge();
            }
            if (electricItemTag.contains("Charge", NbtElement.NUMBER_TYPE)) {
                return electricItemTag.getLong("Charge");
            }
        }
        return 0L;
    }

    @Override
    public boolean setMaxChargeOverride(long newMaxCharge, Simulation simulation) {
        ItemStack oldStack = stackRef.get().copy();
        ItemStack newStack = oldStack.split(1);

        if (newMaxCharge != this.maxCharge) {
            NbtCompound electricItemTag = newStack.getOrCreateSubTag(SUBTAG_NAME);

            if (newMaxCharge == Integer.MAX_VALUE) {
                electricItemTag.remove("MaxCharge");
                electricItemTag.putBoolean("Infinite", true);
            } else {
                electricItemTag.remove("Infinite");
                electricItemTag.putLong("MaxCharge", newMaxCharge);
            }
        } else {
            removeKeyAndSubTagIfEmpty(newStack, "MaxCharge");
        }

        return setStacks(simulation, oldStack, newStack);
    }

    protected boolean setCharge(long newCharge, Simulation simulation) {
        ItemStack oldStack = stackRef.get().copy();
        ItemStack newStack = oldStack.split(1);

        if (newCharge != 0L) {
            NbtCompound electricItemTag = newStack.getOrCreateSubTag(SUBTAG_NAME);
            electricItemTag.putLong("Charge", newCharge);
        } else {
           removeKeyAndSubTagIfEmpty(newStack, "Charge");
        }

        return setStacks(simulation, oldStack, newStack);
    }

    private void removeKeyAndSubTagIfEmpty(ItemStack stack, String key) {
        NbtCompound electricItemTag = stack.getSubTag(SUBTAG_NAME);

        if (electricItemTag != null) {
            electricItemTag.remove(key);

            if (electricItemTag.isEmpty()) {
                stack.removeSubTag(SUBTAG_NAME);
            }
        }
    }

    protected boolean canChargeItem(VoltageTier chargerTier, TransferLimit transferLimit) {
        boolean canChargeWithTier = chargerTier.getVoltage() >= this.voltageTier.getVoltage();
        boolean canChargeWithTransferLimit = this.rechargable && canChargeWithTier;

        return transferLimit.isIgnoreTransferLimit() || canChargeWithTransferLimit;
    }

    protected boolean canDischargeItem(VoltageTier dischargerTier, TransferLimit transferLimit, DischargeMode dischargeMode) {
        boolean canDischargeWithTier = dischargerTier.getVoltage() >= this.voltageTier.getVoltage();
        boolean canDischargeInMode = this.canProvideEnergyExternally || dischargeMode.isInternal();
        boolean canDischargeWithTransferLimit = canDischargeInMode && canDischargeWithTier;

        return transferLimit.isIgnoreTransferLimit() || canDischargeWithTransferLimit;
    }

    protected long applyTransferLimit(long maxAmount, TransferLimit transferLimit) {
        if (transferLimit.isRespectTransferLimit()) {
            return Math.min(maxAmount, this.voltageTier.getVoltage());
        }
        return maxAmount;
    }

    @Override
    public long charge(long maxAmount, VoltageTier chargerTier, TransferLimit transferLimit, Simulation simulation) {
        boolean canChargeItem = canChargeItem(chargerTier, transferLimit);
        long chargeAmount = applyTransferLimit(maxAmount, transferLimit);

        if (canChargeItem && chargeAmount > 0L) {
            long currentCharge = getCharge();
            long maxCharge = getMaxCharge();
            long maxChargeCapacity = maxCharge - currentCharge;

            if (maxChargeCapacity > 0L) {
                long actualChargeAmount = Math.min(chargeAmount, maxChargeCapacity);
                long newItemCharge = currentCharge + actualChargeAmount;

                if (setCharge(newItemCharge, simulation)) {
                    return actualChargeAmount;
                }
            }
        }
        return 0;
    }

    @Override
    public long discharge(long maxAmount, VoltageTier dischargerTier, TransferLimit transferLimit, DischargeMode mode, Simulation simulation) {
        boolean canDischargeItem = canDischargeItem(dischargerTier, transferLimit, mode);
        long dischargeAmount = applyTransferLimit(maxAmount, transferLimit);

        if (canDischargeItem && dischargeAmount > 0L) {
            long currentCharge = getCharge();
            long actualDischargeAmount = Math.min(currentCharge, dischargeAmount);

            if (actualDischargeAmount > 0L) {
                long newItemCharge = currentCharge - actualDischargeAmount;

                if (setCharge(newItemCharge, simulation)) {
                    return actualDischargeAmount;
                }
            }
        }
        return 0L;
    }
}
