package gregtech.api.gui;

import net.minecraft.network.PacketByteBuf;

public final class ClickData {
    public final int button;
    public final boolean isShiftClick;
    public final boolean isCtrlClick;

    public ClickData(int button, boolean isShiftClick, boolean isCtrlClick) {
        this.button = button;
        this.isShiftClick = isShiftClick;
        this.isCtrlClick = isCtrlClick;
    }

    public void writeToBuf(PacketByteBuf buf) {
        buf.writeVarInt(button);
        buf.writeBoolean(isShiftClick);
        buf.writeBoolean(isCtrlClick);
    }

    public static ClickData readFromBuf(PacketByteBuf buf) {
        int button = buf.readVarInt();
        boolean shiftClick = buf.readBoolean();
        boolean ctrlClick = buf.readBoolean();
        return new ClickData(button, shiftClick, ctrlClick);
    }
}
