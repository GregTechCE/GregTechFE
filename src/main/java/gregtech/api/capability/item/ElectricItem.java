package gregtech.api.capability.item;

import alexiil.mc.lib.attributes.Simulation;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;

import java.util.List;

public interface ElectricItem {

    /**
     * Charge an item with a specified amount of energy.
     *
     * @param maxAmount           max amount of energy to charge in EU
     * @param chargerTier         tier of the charging device, has to be at least as high as the item to charge
     * @param ignoreTransferLimit ignore any transfer limits, infinite charge rate
     * @param simulate            don't actually change the item, just determine the return value
     * @return Energy transferred into the electric item
     */
    long charge(long maxAmount, int chargerTier, boolean ignoreTransferLimit, Simulation simulate);

    /**
     * Discharge an item by a specified amount of energy
     * <p>
     * The externally parameter is used to prevent non-battery-like items from providing power. For
     * example discharge slots set externally to true, but items using energy for themselves don't.
     * Special cases like the nano saber hitting armor will discharge with externally = false.
     *
     * @param maxAmount           max amount of energy to discharge in EU
     * @param dischargerTier      tier of the discharging device, has to be at least as high as the item to discharge
     * @param ignoreTransferLimit ignore any transfer limits, infinite discharge rate
     * @param externally          use the supplied item externally, i.e. to power something else as if it was a battery
     * @param simulate            don't actually discharge the item, just determine the return value
     * @return Energy retrieved from the electric item
     */
    long discharge(long maxAmount, int dischargerTier, boolean ignoreTransferLimit, boolean externally, Simulation simulate);

    /**
     * Determines if item can provide external discharging capability "in general"
     * it ensures it can be inserted into battery discharger slots & so
     *
     * @return true if item can be discharged externally
     */
    boolean canProvideChargeExternally();

    /**
     * Determine the charge level for the specified item.
     * <p>
     * The item may not actually be chargeable to the returned level, e.g. if it is a
     * non-rechargeable single use battery.
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
     * Get the tier of the specified item.
     *
     * @return The tier of the item.
     */
    int getTier();

    /**
     * Sets a new maximum charge on this electric item instance
     * Suggested implementation is to store new max charge in item's NBT tag
     * Items are free to not implement this function and just return false
     *
     * @param newMaxCharge new max charge
     * @return true if max charge modification is successful
     */
    boolean setMaxChargeOverride(long newMaxCharge);

    void addItemTooltip(List<Text> tooltip, TooltipContext context);

    default void use(long amount) {
        discharge(amount, Integer.MAX_VALUE, true, false, Simulation.ACTION);
    }

    /**
     * Determine if the specified electric item has at least a specific amount of EU.
     *
     * @param amount minimum amount of energy required
     * @return true if there's enough energy
     */
    default boolean canUse(long amount) {
        return discharge(amount, Integer.MAX_VALUE, true, false, Simulation.SIMULATE) >= amount;
    }
}
