package gregtech.api.network;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import gregtech.api.gui.UIFactory;
import gregtech.api.gui.UIHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.List;

public class PacketUIOpen<E extends UIHolder> implements NetworkPacket {

    public static final Identifier CHANNEL = new Identifier(GTValues.MODID, "ui_open");

    public final UIFactory<E> uiFactory;
    public final byte[] uiHolderData;
    public final int windowId;
    public final List<PacketUIWidgetUpdate> initialWidgetUpdates;

    public PacketUIOpen(UIFactory<E> uiFactory, E uiHolder, int windowId, List<PacketUIWidgetUpdate> initialWidgetUpdates) {
        this.uiFactory = uiFactory;
        this.windowId = windowId;

        ByteBuf rawBuffer = Unpooled.buffer();
        try {
            PacketByteBuf packetBuffer = new PacketByteBuf(rawBuffer);
            uiFactory.writeHolderToSyncData(packetBuffer, uiHolder);

            this.uiHolderData = new byte[rawBuffer.readableBytes()];
            rawBuffer.readBytes(this.uiHolderData);
        } finally {
            rawBuffer.release();
        }
        this.initialWidgetUpdates = initialWidgetUpdates;
    }

    @SuppressWarnings("unchecked")
    public PacketUIOpen(PacketByteBuf packet) {
        int uiFactoryId = packet.readVarInt();
        this.uiFactory = (UIFactory<E>) UIFactory.REGISTRY.get(uiFactoryId);
        Preconditions.checkNotNull(uiFactory, "UI Factory wth ID %d is not found", uiFactoryId);

        this.windowId = packet.readVarInt();
        this.uiHolderData = packet.readByteArray();

        this.initialWidgetUpdates = packet.readList(buf -> {
            int widgetId = buf.readVarInt();
            byte[] updateData = buf.readByteArray();

            return new PacketUIWidgetUpdate(windowId, widgetId, updateData);
        });
    }

    @Override
    public void writeBuffer(PacketByteBuf packet) {
        int uiFactoryId = UIFactory.REGISTRY.getRawId(uiFactory);
        packet.writeVarInt(uiFactoryId);

        packet.writeVarInt(this.windowId);
        packet.writeByteArray(this.uiHolderData);

        packet.writeCollection(initialWidgetUpdates, (buf, update) -> {
            buf.writeVarInt(update.widgetId);
            buf.writeByteArray(update.updateData);
        });
    }

    @Override
    public Identifier getChannel() {
        return CHANNEL;
    }
}
