package gregtech.api.capability.item;

import alexiil.mc.lib.attributes.Simulation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to be implemented on items which would like to have custom damage handling
 * and ability to deny damage when there's no durability left instead of always breaking item,
 * plus also offers ability to return a completely different item on break/damage
 */
public interface CustomDamageItem {

    /**
     * Attempts to damage the item
     * @param itemStack item stack being damaged
     * @param damage amount of damage to deal, in durability points
     * @param simulate whenever an action should be simulated
     * @return  whenever damaging was successful and resulting item stack
     */
    Pair<ItemDamageResult, ItemStack> attemptDamageItem(ItemStack itemStack, int damage, @Nullable LivingEntity entity, Simulation simulate);

    default boolean canDamageItem(ItemStack itemStack, int damage, @Nullable LivingEntity playerEntity) {
        return attemptDamageItem(itemStack, damage, playerEntity, Simulation.SIMULATE).getLeft().isSuccess();
    }

    /**
     * Possible results of item damaging
     */
    enum ItemDamageResult {

        /** Item cannot be damaged, for example because it's fully discharged */
        CANNOT_DAMAGE,

        /** Item has been damaged and still has durability remaining */
        DAMAGED,

        /** Damage is successful, item is broken as the result */
        BROKEN;

        /**
         * Returns true if item has been damaged successfully
         */
        public boolean isSuccess() {
            return this == DAMAGED || this == BROKEN;
        }
    }

    /**
     * Attempts to damage item in the player's hand, taking CustomDamageItem
     * implementations into account and falling back to ItemStack.damageItem for normal items
     * Simulation can be used to determine whenever item can be damaged
     * Will also play tool break animation when tool is broken.
     *
     * @param entity player entity
     * @param hand the hand at which the item is located in
     * @param damage amount of damage to deal
     * @param simulate whenever action is simulated
     * @return whenever item could be damaged
     */
    static boolean damageItem(LivingEntity entity, Hand hand, int damage, Simulation simulate) {
        ItemStack itemStack = entity.getStackInHand(hand);

        //Check for CustomDamageItem instance first
        if (itemStack.getItem() instanceof CustomDamageItem customDamageItem) {
            Pair<ItemDamageResult, ItemStack> result = customDamageItem.attemptDamageItem(itemStack, damage, entity, simulate);

            ItemDamageResult damageResult = result.getLeft();
            ItemStack resultStack = result.getRight();

            //Update item in player's hand if damage is successful and we're not doing a simulation
            if (damageResult.isSuccess() && simulate.isAction()) {
                entity.setStackInHand(hand, resultStack);

                //Play tool break animation if tool is broken now
                if (damageResult == ItemDamageResult.BROKEN) {
                    if (entity instanceof PlayerEntity playerEntity) {
                        playerEntity.incrementStat(Stats.BROKEN.getOrCreateStat(itemStack.getItem()));
                    }
                    entity.sendToolBreakStatus(hand);
                }
            }
            return damageResult.isSuccess();
        }

        //Perform normal item stack damage logic
        itemStack.damage(damage, entity, sameEntity -> sameEntity.sendToolBreakStatus(hand));
        return true;
    }
}
