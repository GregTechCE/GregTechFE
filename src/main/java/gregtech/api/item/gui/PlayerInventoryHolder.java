package gregtech.api.item.gui;

import com.google.common.base.Preconditions;
import gregtech.api.gui.UIFactories;
import gregtech.api.gui.UIHolder;
import gregtech.api.gui.ModularUI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class PlayerInventoryHolder implements UIHolder {

    private final PlayerEntity player;
    private final Hand hand;
    private ItemStack originalItem;

    PlayerInventoryHolder(PlayerEntity player, Hand hand, ItemStack originalItem) {
        this.player = player;
        this.hand = hand;
        this.originalItem = originalItem;
    }

    private PlayerInventoryHolder(PlayerEntity player, Hand hand) {
        this.player = player;
        this.hand = hand;
        this.originalItem = player.getStackInHand(hand);
        Preconditions.checkArgument(originalItem.getItem() instanceof ItemUIFactory,
                "Item %s should implement ItemUIFactory", originalItem.getItem());
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public Hand getHand() {
        return hand;
    }

    public ItemStack getOriginalItem() {
        return originalItem;
    }

    public static void openItemUI(ServerPlayerEntity player, Hand hand) {
        PlayerInventoryHolder holder = new PlayerInventoryHolder(player, hand);
        UIFactories.PLAYER_INVENTORY.openUI(holder, player);
    }

    ModularUI createUI(PlayerEntity entityPlayer) {
        ItemUIFactory uiFactory = (ItemUIFactory) originalItem.getItem();
        return uiFactory.createUI(this, entityPlayer);
    }

    @Override
    public boolean isValid() {
        ItemStack itemStack = player.getStackInHand(hand);
        return this.originalItem.isItemEqual(itemStack);
    }

    @Override
    public boolean isClient() {
        return this.player.world.isClient();
    }

    @Override
    public void markDirty() {
        this.player.getInventory().markDirty();
        ScreenHandler screenHandler = this.player.currentScreenHandler;

        if (screenHandler != null) {
            screenHandler.sendContentUpdates();
            screenHandler.syncState();
        }
    }

    public ItemStack getActiveItem() {
        ItemStack itemStack = this.player.getStackInHand(hand);

        if (!this.originalItem.isItemEqual(itemStack)) {
            return ItemStack.EMPTY;
        }
        return itemStack;
    }

    /**
     * Will replace current item in hand with the given one
     * will also update sample item to this item
     */
    public void setActiveItem(ItemStack itemStack) {
        ItemStack currentStack = this.player.getStackInHand(hand);
        if (!this.originalItem.isItemEqual(currentStack)) {
            return;
        }

        Preconditions.checkArgument(itemStack.getItem() instanceof ItemUIFactory,
            "Item %s should implement ItemUIFactory", itemStack.getItem());
        this.originalItem = itemStack;
        player.setStackInHand(hand, itemStack);
    }
}
