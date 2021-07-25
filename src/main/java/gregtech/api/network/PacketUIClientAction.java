package gregtech.api.network;

import gregtech.api.GTValues;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class PacketUIClientAction implements NetworkPacket {

    public static final Identifier CHANNEL = new Identifier(GTValues.MODID, "ui_client_action");

    public final int windowId;
    public final int widgetId;
    public final byte[] updateData;

    public PacketUIClientAction(int windowId, int widgetId, byte[] updateData) {
        this.windowId = windowId;
        this.widgetId = widgetId;
        this.updateData = updateData;
    }

    public PacketUIClientAction(PacketByteBuf packet) {
        this.windowId = packet.readVarInt();
        this.widgetId = packet.readVarInt();
        this.updateData = packet.readByteArray();
    }

    @Override
    public void writeBuffer(PacketByteBuf buffer) {
        buffer.writeVarInt(this.windowId);
        buffer.writeVarInt(this.widgetId);
        buffer.writeByteArray(this.updateData);
    }

    @Override
    public Identifier getChannel() {
        return CHANNEL;
    }
}
