package gregtech.api.net;

import gregtech.api.GTValues;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class PacketClipboard implements NetworkPacket {

    public static final Identifier CHANNEL = new Identifier(GTValues.MODID, "clipboard");

    public final String text;

    public PacketClipboard(final String text) {
        this.text = text;
    }

    public PacketClipboard(PacketByteBuf packet) {
        this.text = packet.readString(256);
    }

    @Override
    public void writeBuffer(PacketByteBuf buffer) {
        buffer.writeString(text);
    }

    @Override
    public Identifier getChannel() {
        return CHANNEL;
    }
}
