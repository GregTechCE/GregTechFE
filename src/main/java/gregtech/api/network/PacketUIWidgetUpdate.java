package gregtech.api.network;

import gregtech.api.GTValues;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class PacketUIWidgetUpdate implements NetworkPacket {

    public static final Identifier CHANNEL = new Identifier(GTValues.MODID, "ui_widget_update");

    public final int windowId;
    public final int widgetId;
    public final byte[] updateData;

    public PacketUIWidgetUpdate(int windowId, int widgetId, byte[] updateData) {
        this.windowId = windowId;
        this.widgetId = widgetId;
        this.updateData = updateData;
    }

    public PacketUIWidgetUpdate(PacketByteBuf packet) {
        this.windowId = packet.readVarInt();
        this.widgetId = packet.readVarInt();
        this.updateData = packet.readByteArray();
    }

    @Override
    public void writeBuffer(PacketByteBuf buffer) {
        buffer.writeVarInt(windowId);
        buffer.writeVarInt(widgetId);
        buffer.writeByteArray(this.updateData);
    }

    @Override
    public Identifier getChannel() {
        return CHANNEL;
    }
}
