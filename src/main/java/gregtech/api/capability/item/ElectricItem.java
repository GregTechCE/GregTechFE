package gregtech.api.capability.item;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.util.VoltageTier;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;

import java.util.List;

public interface ElectricItem {

    /**
     * Charge an item with a specified amount of energy
     *
     * @param maxAmount max amount of energy to charge in EU
     * @param chargerTier tier of the charging device
     * @param transferLimit transfer limit mode
     * @param simulate whenever action is simulated
     * @return amount of energy consumed by the electric item
     */
    long charge(long maxAmount, VoltageTier chargerTier, TransferLimit transferLimit, Simulation simulate);

    /**
     * Discharge an item by a specified amount of energy
     *
     * @param maxAmount max amount of energy to discharge in EU
     * @param dischargerTier tier of the discharging device
     * @param transferLimit transfer limit mode
     * @param mode determines whenever item's charge is used externally, e.g. as a battery, or internally
     * @param simulate whenever action is actually simulated
     * @return Energy retrieved from the electric item
     */
    long discharge(long maxAmount, VoltageTier dischargerTier, TransferLimit transferLimit, DischargeMode mode, Simulation simulate);

    /**
     * Determines if item can provide external discharging capability in general,
     * so it can be inserted into discharger slots and have other battery-alike behaviors
     *
     * @return true if item can be discharged externally
     */
    boolean canProvideChargeExternally();

    /**
     * Determine the charge level for the specified item
     * The item may not actually be chargeable to the returned level, for example
     * in the case of single-use battery
     *
     * @return maximum charge level in EU
     */
    long getMaxCharge();

    /**
     * Determine the current charge for the specified item
     *
     * @return current charge level in EU
     */
    long getCharge();

    /**
     * Retrieves the voltage tier of the item
     * Returned value is used to check whenever item can be inserted
     * into the chargers of the certain tier or not
     *
     * @return tier of this electric item
     */
    VoltageTier getVoltageTier();

    /**
     * Internally discharges item by the specified amount of energy, ignoring transfer limit
     * Does not actually check whenever item had enough charge, call {@link #canUse(long)} beforehand
     * to determine whenever energy is actually available
     *
     * @param amount amount of energy to discharge
     */
    default void use(long amount) {
        discharge(amount, VoltageTier.MAX, TransferLimit.IGNORE, DischargeMode.INTERNAL, Simulation.ACTION);
    }

    /**
     * Determine if the specified electric item has at least a specific amount of EU
     *
     * @param amount minimum amount of energy required
     * @return true if there's enough energy
     */
    default boolean canUse(long amount) {
        return discharge(amount, VoltageTier.MAX, TransferLimit.IGNORE, DischargeMode.INTERNAL, Simulation.SIMULATE) == amount;
    }

    /**
     * Sets a new maximum charge on this electric item instance
     * Suggested implementation is to store new max charge in item's NBT tag
     * Items are free to not implement this function and just return false
     *
     * @param newMaxCharge new max charge
     * @param simulation whenever action is simulated
     * @return true if max charge modification is successful
     */
    default boolean setMaxChargeOverride(long newMaxCharge, Simulation simulation) {
        return false;
    }

    /**
     * Adds tooltip providing statistics this electric item has,
     * for example it's voltage tier, maximum charge and amount of energy currently stored
     * override this to customize the behavior
     *
     * @param tooltip result tooltip
     * @param context tooltip context (whenever advanced tooltips are enabled)
     */
    default void addItemTooltip(List<Text> tooltip, TooltipContext context) {
    }
}
