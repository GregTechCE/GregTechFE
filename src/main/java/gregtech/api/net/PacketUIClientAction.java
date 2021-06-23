package gregtech.api.net;

import gregtech.api.GTValues;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class PacketUIClientAction implements NetworkPacket {

    public static final Identifier CHANNEL = new Identifier(GTValues.MODID, "client_action");

    public final int windowId;
    public final int widgetId;
    public final PacketByteBuf updateData;

    public PacketUIClientAction(int windowId, int widgetId, PacketByteBuf updateData) {
        this.windowId = windowId;
        this.widgetId = widgetId;
        this.updateData = updateData;
    }

    public PacketUIClientAction(PacketByteBuf packet) {
        this.windowId = packet.readVarInt();
        this.widgetId = packet.readVarInt();

        int readableBytes = packet.readVarInt();
        this.updateData = PacketByteBufs.create();
        packet.readBytes(updateData, readableBytes);
    }

    @Override
    public void writeBuffer(PacketByteBuf buffer) {
        buffer.writeVarInt(windowId);
        buffer.writeVarInt(widgetId);

        buffer.writeVarInt(updateData.readableBytes());
        buffer.writeBytes(updateData);
    }

    @Override
    public Identifier getChannel() {
        return CHANNEL;
    }
}
