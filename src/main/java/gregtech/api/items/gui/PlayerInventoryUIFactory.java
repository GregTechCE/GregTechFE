package gregtech.api.items.gui;

import com.google.common.base.Preconditions;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.UIFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;

public class PlayerInventoryUIFactory extends UIFactory<PlayerInventoryHolder> {

    @Override
    protected ModularUI createUITemplate(PlayerInventoryHolder holder, PlayerEntity entityPlayer) {
        return holder.createUI(entityPlayer);
    }

    @Override
    public PlayerInventoryHolder readHolderFromSyncData(PacketByteBuf syncData) {
        UUID playerId = syncData.readUuid();
        Hand hand = syncData.readEnumConstant(Hand.class);
        ItemStack originalItem = syncData.readItemStack();

        MinecraftClient client = MinecraftClient.getInstance();
        World world = Preconditions.checkNotNull(client.world);

        PlayerEntity playerEntity = world.getPlayerByUuid(playerId);
        Preconditions.checkNotNull(playerEntity, "Cannot find player to open Item UI with id " + playerId);

        return new PlayerInventoryHolder(playerEntity, hand, originalItem);
    }

    @Override
    public void writeHolderToSyncData(PacketByteBuf syncData, PlayerInventoryHolder holder) {
        syncData.writeUuid(holder.getPlayer().getGameProfile().getId());
        syncData.writeEnumConstant(holder.getHand());
        syncData.writeItemStack(holder.getOriginalItem());
    }
}
