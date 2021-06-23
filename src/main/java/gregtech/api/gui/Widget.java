package gregtech.api.gui;

import com.google.common.base.Preconditions;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Widget is functional element of ModularUI
 * It can draw, perform actions, react to key press and mouse
 * It's information is also synced to client
 */
public abstract class Widget {

    protected ModularUI gui;
    protected SizeProvider sizes;
    protected WidgetUIAccess uiAccess;
    private Position parentPosition = Position.ORIGIN;
    private Position selfPosition;
    private Position position;
    private Size size;

    public Widget(Position selfPosition, Size size) {
        Preconditions.checkNotNull(selfPosition, "selfPosition");
        Preconditions.checkNotNull(size, "size");
        this.selfPosition = selfPosition;
        this.size = size;
        this.position = this.parentPosition.add(selfPosition);
    }

    public void setGui(ModularUI gui) {
        this.gui = gui;
    }

    public void setSizes(SizeProvider sizes) {
        this.sizes = sizes;
    }

    public void setUiAccess(WidgetUIAccess uiAccess) {
        this.uiAccess = uiAccess;
    }

    public void setParentPosition(Position parentPosition) {
        Preconditions.checkNotNull(parentPosition, "parentPosition");
        this.parentPosition = parentPosition;
        recomputePosition();
    }

    protected void setSelfPosition(Position selfPosition) {
        Preconditions.checkNotNull(selfPosition, "selfPosition");
        this.selfPosition = selfPosition;
        recomputePosition();
    }

    protected void setSize(Size size) {
        Preconditions.checkNotNull(size, "size");
        this.size = size;
        onSizeUpdate();
    }

    public final Position getPosition() {
        return position;
    }

    public final Size getSize() {
        return size;
    }

    public Rectangle toRectangleBox() {
        Position pos = getPosition();
        Size size = getSize();
        return new Rectangle(pos.x, pos.y, size.width, size.height);
    }

    private void recomputePosition() {
        this.position = this.parentPosition.add(selfPosition);
        onPositionUpdate();
    }

    public void applyScissor(final int parentX, final int parentY, final int parentWidth, final int parentHeight) {
    }

    protected void onPositionUpdate() {
    }

    protected void onSizeUpdate() {
    }

    public boolean isMouseOverElement(int mouseX, int mouseY, boolean correctPositionOnMouseWheelMoveEvent) {
        mouseX = correctPositionOnMouseWheelMoveEvent ? mouseX + getPosition().x : mouseX;
        return isMouseOverElement(mouseX, mouseY);
    }

    public boolean isMouseOverElement(int mouseX, int mouseY) {
        Position position = getPosition();
        Size size = getSize();
        return isMouseOver(position.x, position.y, size.width, size.height, mouseX, mouseY);
    }

    public static boolean isMouseOver(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && x + width > mouseX && y + height > mouseY;
    }

    /**
     * Called on both sides to initialize widget data
     */
    public void initWidget() {
    }

    /**
     * Called on serverside to detect changes and synchronize them with clients
     */
    public void detectAndSendChanges() {
    }

    /**
     * Called clientside every tick with this modular UI open
     */
    @Environment(EnvType.CLIENT)
    public void updateScreen() {
    }

    /**
     * Called each draw tick to draw this widget in GUI
     */
    @Environment(EnvType.CLIENT)
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY, RenderContext renderContext) {
    }

    /**
     * Called each draw tick to draw this widget in GUI
     */
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
    }

    /**
     * Called when mouse wheel is moved in GUI
     */
    @Environment(EnvType.CLIENT)
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return false;
    }

    /**
     * Called when mouse is clicked in GUI
     */
    @Environment(EnvType.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Called when mouse is pressed and hold down in GUI
     */
    @Environment(EnvType.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    /**
     * Called when mouse is released in GUI
     */
    @Environment(EnvType.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    /**
     * Read data received from server's {@link #writeUpdateInfo}
     */
    @Environment(EnvType.CLIENT)
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
    }

    public void handleClientAction(int id, PacketByteBuf buffer) {
    }

    public List<NativeWidget> getNativeWidgets() {
        if (this instanceof NativeWidget) {
            return Collections.singletonList((NativeWidget) this);
        }
        return Collections.emptyList();
    }

    /**
     * Writes data to be sent to client's {@link #readUpdateInfo}
     */
    protected final void writeUpdateInfo(int id, Consumer<PacketByteBuf> packetBufferWriter) {
        if (uiAccess != null && gui != null) {
            uiAccess.writeUpdateInfo(this, id, packetBufferWriter);
        }
    }

    @Environment(EnvType.CLIENT)
    protected final void writeClientAction(int id, Consumer<PacketByteBuf> packetBufferWriter) {
        if (uiAccess != null) {
            uiAccess.writeClientAction(this, id, packetBufferWriter);
        }
    }

    protected static boolean isClientSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
