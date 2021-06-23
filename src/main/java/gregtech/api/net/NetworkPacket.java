package gregtech.api.net;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface NetworkPacket {

    void writeBuffer(PacketByteBuf buffer);

    Identifier getChannel();

    default void sendTo(ServerPlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create();
        writeBuffer(buffer);
        ServerPlayNetworking.send(player, getChannel(), buffer);
    }

    @Environment(EnvType.CLIENT)
    default void sendToClient() {
        PacketByteBuf buffer = PacketByteBufs.create();
        writeBuffer(buffer);
        ClientPlayNetworking.send(getChannel(), buffer);
    }
}
