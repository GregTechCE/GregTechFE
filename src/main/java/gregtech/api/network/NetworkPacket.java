package gregtech.api.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public interface NetworkPacket {

    void writeBuffer(PacketByteBuf buffer);

    Identifier getChannel();

    private Packet<?> toMCPacket() {
        PacketByteBuf buffer = PacketByteBufs.create();
        writeBuffer(buffer);
        return ServerPlayNetworking.createS2CPacket(getChannel(), buffer);
    }

    default void sendTo(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(toMCPacket());
    }

    default void sendToAll(MinecraftServer server) {
        server.getPlayerManager().sendToAll(toMCPacket());
    }

    default void sendToDimension(ServerWorld world) {
        MinecraftServer server = world.getServer();
        RegistryKey<World> registryKey = world.getRegistryKey();
        server.getPlayerManager().sendToDimension(toMCPacket(), registryKey);
    }

    default void sendToAllAround(ServerWorld world, Vec3d location, double distance) {
        MinecraftServer server = world.getServer();
        RegistryKey<World> registryKey = world.getRegistryKey();
        server.getPlayerManager().sendToAround(null, location.getX(), location.getY(), location.getZ(), distance, registryKey, toMCPacket());
    }

    @Environment(EnvType.CLIENT)
    default void sendToClient() {
        PacketByteBuf buffer = PacketByteBufs.create();
        writeBuffer(buffer);
        ClientPlayNetworking.send(getChannel(), buffer);
    }
}
