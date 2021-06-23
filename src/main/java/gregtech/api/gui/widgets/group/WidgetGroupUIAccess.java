package gregtech.api.gui.widgets.group;

import gregtech.api.gui.Widget;
import gregtech.api.gui.WidgetUIAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Consumer;

class WidgetGroupUIAccess implements WidgetUIAccess {

    private final AbstractWidgetGroup widgetGroup;

    public WidgetGroupUIAccess(AbstractWidgetGroup widgetGroup) {
        this.widgetGroup = widgetGroup;
    }

    @Override
    public void notifySizeChange() {
        WidgetUIAccess uiAccess = widgetGroup.getUIAccess();
        widgetGroup.recomputeSize();
        if (uiAccess != null) {
            uiAccess.notifySizeChange();
        }
    }

    @Override
    public boolean attemptMergeStack(ItemStack itemStack, boolean fromContainer, boolean simulate) {
        WidgetUIAccess uiAccess = widgetGroup.getUIAccess();
        if (uiAccess != null) {
            return uiAccess.attemptMergeStack(itemStack, fromContainer, simulate);
        }
        return false;
    }

    @Override
    public void notifyWidgetChange() {
        WidgetUIAccess uiAccess = widgetGroup.getUIAccess();
        if (uiAccess != null) {
            uiAccess.notifyWidgetChange();
        }
        widgetGroup.recomputeSize();
    }

    @Override
    public void writeClientAction(Widget widget, int updateId, Consumer<PacketByteBuf> dataWriter) {
        WidgetUIAccess uiAccess = widgetGroup.getUIAccess();

        uiAccess.writeClientAction(widgetGroup, 1, buffer -> {
            buffer.writeVarInt(widgetGroup.widgets.indexOf(widget));
            buffer.writeVarInt(updateId);
            dataWriter.accept(buffer);
        });
    }

    @Override
    public void writeUpdateInfo(Widget widget, int updateId, Consumer<PacketByteBuf> dataWriter) {
        WidgetUIAccess uiAccess = widgetGroup.getUIAccess();

        uiAccess.writeUpdateInfo(widgetGroup, 1, buffer -> {
            buffer.writeVarInt(widgetGroup.widgets.indexOf(widget));
            buffer.writeVarInt(updateId);
            dataWriter.accept(buffer);
        });
    }
}
