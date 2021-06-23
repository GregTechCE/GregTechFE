package gregtech.api.gui;

import gregtech.api.gui.NativeWidget;
import gregtech.api.gui.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Consumer;

/**
 * Provides access to UI's syncing mechanism, allowing to send arbitrary data and
 * receive it on the client or server side via callbacks provided to widgets
 * IDs are used only by your own widget, use IDs you like
 */
public interface WidgetUIAccess {

    void notifySizeChange();

    /**
     * Call when widget is added/removed, or INativeWidget list changed
     * and should be updated accordingly
     */
    void notifyWidgetChange();

    /**
     * Attempts to perform a slot merging (shift-click) for a given stack
     * with the slots either from player inventory, or a container
     * @return true if stack was merged
     */
    boolean attemptMergeStack(ItemStack itemStack, boolean fromContainer, boolean simulate);

    /**
     * Sends action to the server with the ID and data payload supplied
     * Server will receive it in {@link Widget#handleClientAction(int, PacketByteBuf)}
     */
    void writeClientAction(Widget widget, int id, Consumer<PacketByteBuf> payloadWriter);

    /**
     * Sends update to the client from the server
     * Usually called when internal state changes in {@link Widget#detectAndSendChanges()}
     * Client will receive payload data in {@link Widget#readUpdateInfo(int, PacketByteBuf)}
     */
    void writeUpdateInfo(Widget widget, int id, Consumer<PacketByteBuf> payloadWriter);

}
