package gregtech.api.network;

import gregtech.api.GTValues;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PacketWorldEvent implements NetworkPacket {

    public static final Identifier CHANNEL = new Identifier(GTValues.MODID, "world_event");

    private final int eventId;
    private final BlockPos pos;
    private final int data;

    public PacketWorldEvent(int eventId, BlockPos pos, int data) {
        this.eventId = eventId;
        this.pos = pos;
        this.data = data;
    }

    public PacketWorldEvent(PacketByteBuf buffer) {
        this.eventId = buffer.readVarInt();
        this.pos = buffer.readBlockPos();
        this.data = buffer.readVarInt();
    }

    public int getEventId() {
        return eventId;
    }

    public BlockPos getPos() {
        return pos;
    }

    public int getData() {
        return data;
    }

    @Override
    public void writeBuffer(PacketByteBuf buffer) {
        buffer.writeVarInt(this.eventId);
        buffer.writeBlockPos(this.pos);
        buffer.writeVarInt(this.data);
    }

    @Override
    public Identifier getChannel() {
        return CHANNEL;
    }
}
