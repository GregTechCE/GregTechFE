package gregtech.api.net;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import gregtech.api.gui.UIHolder;
import gregtech.api.gui.UIFactory;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.List;

public class PacketUIOpen<E extends UIHolder> implements NetworkPacket {

    public static final Identifier CHANNEL = new Identifier(GTValues.MODID, "ui_open");

    public final UIFactory<E> uiFactory;
    public final E uiHolder;
    public final int windowId;
    public final List<PacketUIWidgetUpdate> initialWidgetUpdates;

    public PacketUIOpen(UIFactory<E> uiFactory, E uiHolder, int windowId, List<PacketUIWidgetUpdate> initialWidgetUpdates) {
        this.uiFactory = uiFactory;
        this.uiHolder = uiHolder;
        this.windowId = windowId;
        this.initialWidgetUpdates = initialWidgetUpdates;
    }

    @SuppressWarnings("unchecked")
    public PacketUIOpen(PacketByteBuf packet) {
        int uiFactoryId = packet.readVarInt();
        this.uiFactory = (UIFactory<E>) UIFactory.REGISTRY.get(uiFactoryId);
        Preconditions.checkNotNull(uiFactory);

        this.uiHolder = uiFactory.readHolderFromSyncData(packet);
        this.windowId = packet.readVarInt();

        this.initialWidgetUpdates = packet.readList(buf -> {
            int widgetId = buf.readVarInt();
            int readableBytes = buf.readVarInt();

            PacketByteBuf updateData = PacketByteBufs.create();
            buf.readBytes(updateData, readableBytes);
            return new PacketUIWidgetUpdate(windowId, widgetId, updateData);
        });
    }

    @Override
    public void writeBuffer(PacketByteBuf packet) {
        int uiFactoryId = UIFactory.REGISTRY.getRawId(uiFactory);
        packet.writeVarInt(uiFactoryId);

        this.uiFactory.writeHolderToSyncData(packet, this.uiHolder);
        packet.writeVarInt(this.windowId);

        packet.writeCollection(initialWidgetUpdates, (buf, update) -> {
            buf.writeVarInt(update.widgetId);
            buf.writeVarInt(update.updateData.readableBytes());
            buf.writeBytes(update.updateData);
        });
    }

    @Override
    public Identifier getChannel() {
        return CHANNEL;
    }
}
