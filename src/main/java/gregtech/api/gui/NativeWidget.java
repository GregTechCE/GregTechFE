package gregtech.api.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ClickType;

/**
 * Native widget is widget wrapping native Slot
 * That means controls are delegated to vanilla {@link net.minecraft.screen.ScreenHandler}
 * Rendering is still handled by widget via helpers in {@link RenderContext}
 */
public interface NativeWidget extends EnableNotifiedWidget {

    @Override
    void setEnabled(boolean isEnabled);

    /**
     * You should return MC slot handle instance you created earlier
     *
     * @return MC slot
     */
    Slot getHandle();

    /**
     * @return true if this slot belongs to player inventory
     */
    SlotLocationInfo getSlotLocationInfo();

    /**
     * @return true when this slot is valid for double click merging
     */
    boolean canMergeSlot(ItemStack stack);

    /**
     * Called when item is taken from the slot
     * Simulated take is used to compute slot merging behavior
     * This method should not modify slot state if it is simulated
     */
    default void onItemTake(PlayerEntity player, ItemStack stack, boolean simulate) {
    }

    /**
     * Called when slot is clicked in Container
     * Return true to cancel vanilla logic on the slot click handling
     */
    boolean onSlotClick(int button, SlotActionType actionType, PlayerEntity player);

    class SlotLocationInfo {
        public final boolean isPlayerInventory;
        public final boolean isHotbarSlot;

        public SlotLocationInfo(boolean isPlayerInventory, boolean isHotbarSlot) {
            this.isPlayerInventory = isPlayerInventory;
            this.isHotbarSlot = isHotbarSlot;
        }
    }
}
