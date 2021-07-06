package gregtech.api.gui.widgets.group;

import gregtech.api.gui.NativeWidget;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.WidgetUIAccess;
import gregtech.api.gui.igredient.IGhostIngredientTarget;
import gregtech.api.gui.igredient.IIngredientSlot;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbstractWidgetGroup extends Widget implements IGhostIngredientTarget, IIngredientSlot {

    protected final List<Widget> widgets = new ArrayList<>();
    private final WidgetGroupUIAccess groupUIAccess = new WidgetGroupUIAccess(this);
    private boolean isVisible = true;
    private final boolean isDynamicSized;
    private boolean initialized = false;

    public AbstractWidgetGroup(Position position) {
        super(position, Size.ZERO);
        this.isDynamicSized = true;
    }

    public AbstractWidgetGroup(Position position, Size size) {
        super(position, size);
        this.isDynamicSized = false;
    }

    WidgetUIAccess getUIAccess() {
        return uiAccess;
    }

    public List<Widget> getContainedWidgets(boolean includeHidden) {
        ArrayList<Widget> containedWidgets = new ArrayList<>(widgets.size());

        for (Widget widget : widgets) {
            containedWidgets.add(widget);

            if (widget instanceof AbstractWidgetGroup)
                containedWidgets.addAll(((AbstractWidgetGroup) widget).getContainedWidgets(includeHidden));
        }

        return containedWidgets;
    }

    @Override
    protected void onPositionUpdate() {
        Position selfPosition = getPosition();
        for (Widget widget : widgets) {
            widget.setParentPosition(selfPosition);
        }
        recomputeSize();
    }

    public void applyScissor(final int parentX, final int parentY, final int parentWidth, final int parentHeight) {
        for (Widget widget : getContainedWidgets(true)) {
            widget.applyScissor(parentX, parentY, parentWidth, parentHeight);
        }
    }

    protected boolean recomputeSize() {
        if (isDynamicSized) {
            Size currentSize = getSize();
            Size dynamicSize = computeDynamicSize();
            if (!currentSize.equals(dynamicSize)) {
                setSize(dynamicSize);
                if (uiAccess != null)
                    uiAccess.notifySizeChange();
                return true;
            }
        }
        return false;
    }

    protected Size computeDynamicSize() {
        Position selfPosition = getPosition();
        Size currentSize = getSize();
        for (Widget widget : widgets) {
            Position size = widget.getPosition().add(widget.getSize()).subtract(selfPosition);
            if (size.x > currentSize.width) {
                currentSize = new Size(size.x, currentSize.height);
            }
            if (size.y > currentSize.height) {
                currentSize = new Size(currentSize.width, size.y);
            }
        }
        return currentSize;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
        widgets.stream().flatMap(it -> it.getNativeWidgets().stream()).forEach(it -> it.setEnabled(visible));
    }

    public boolean isVisible() {
        return isVisible;
    }

    protected void addWidget(Widget widget) {
        if (widget == this) {
            throw new IllegalArgumentException("Cannot add self");
        }
        if (widgets.contains(widget)) {
            throw new IllegalArgumentException("Already added");
        }
        this.widgets.add(widget);
        widget.setUiAccess(groupUIAccess);
        widget.setGui(gui);
        widget.setSizes(sizes);
        widget.setParentPosition(getPosition());
        if (initialized) {
            widget.initWidget();
        }
        recomputeSize();
        if (uiAccess != null) {
            uiAccess.notifyWidgetChange();
        }
    }

    protected void removeWidget(Widget widget) {
        if (!widgets.contains(widget)) {
            throw new IllegalArgumentException("Not added");
        }
        this.widgets.remove(widget);
        widget.setUiAccess(null);
        widget.setGui(null);
        widget.setSizes(null);
        widget.setParentPosition(Position.ORIGIN);
        recomputeSize();
        if (uiAccess != null) {
            this.uiAccess.notifyWidgetChange();
        }
    }

    protected void clearAllWidgets() {
        this.widgets.forEach(it -> {
            it.setUiAccess(null);
            it.setGui(null);
            it.setSizes(null);
            it.setParentPosition(Position.ORIGIN);
        });
        this.widgets.clear();
        recomputeSize();
        if (uiAccess != null) {
            this.uiAccess.notifyWidgetChange();
        }
    }

    public boolean isWidgetVisible(Widget widget) {
        return this.isVisible;
    }

    public boolean isWidgetClickable(Widget widget) {
        return isWidgetVisible(widget);
    }

    @Override
    public void initWidget() {
        this.initialized = true;
        for (Widget widget : widgets) {
            widget.setGui(gui);
            widget.setSizes(sizes);
            widget.initWidget();
        }
    }

    @Override
    public List<NativeWidget> getNativeWidgets() {
        ArrayList<NativeWidget> nativeWidgets = new ArrayList<>();
        for (Widget widget : widgets) {
            nativeWidgets.addAll(widget.getNativeWidgets());
        }
        if (this instanceof NativeWidget) {
            nativeWidgets.add((NativeWidget) this);
        }
        return nativeWidgets;
    }

    @Override
    public List<Target> getPhantomTargets(Object ingredient) {
        if (!isVisible) {
            return Collections.emptyList();
        }
        ArrayList<Target> targets = new ArrayList<>();
        for (Widget widget : widgets) {
            if (widget instanceof IGhostIngredientTarget) {
                targets.addAll(((IGhostIngredientTarget) widget).getPhantomTargets(ingredient));
            }
        }
        return targets;
    }

    @Override
    public Object getIngredientOverMouse(int mouseX, int mouseY) {
        if (!isVisible) {
            return Collections.emptyList();
        }
        for (Widget widget : widgets) {
            if (widget instanceof IIngredientSlot ingredientSlot) {
                Object result = ingredientSlot.getIngredientOverMouse(mouseX, mouseY);
                if (result != null) return result;
            }
        }
        return null;
    }

    @Override
    public void detectAndSendChanges() {
        for (Widget widget : widgets) {
            widget.detectAndSendChanges();
        }
    }

    @Override
    public void updateScreen() {
        for (Widget widget : widgets) {
            widget.updateScreen();
        }
    }

    @Override
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY, RenderContext renderContext) {
        for (Widget widget : widgets) {
            if (isWidgetVisible(widget)) {
                widget.drawInForeground(matrices, mouseX, mouseY, renderContext);
            }
        }
    }

    @Override
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        for (Widget widget : widgets) {
            if (isWidgetVisible(widget)) {
                widget.drawInBackground(matrices, mouseX, mouseY, deltaTicks, renderContext);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return widgets.stream()
                .filter(this::isWidgetClickable)
                .anyMatch(it -> it.mouseScrolled(mouseX, mouseY, amount));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return widgets.stream()
                .filter(this::isWidgetClickable)
                .anyMatch(it -> it.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return widgets.stream()
                .filter(this::isWidgetClickable)
                .anyMatch(it -> it.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return widgets.stream()
                .filter(this::isWidgetClickable)
                .anyMatch(it -> it.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return widgets.stream()
                .filter(this::isWidgetClickable)
                .anyMatch(it -> it.keyPressed(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return widgets.stream()
                .filter(this::isWidgetClickable)
                .anyMatch(it -> it.keyReleased(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return widgets.stream()
                .filter(this::isWidgetClickable)
                .anyMatch(it -> it.charTyped(chr, modifiers));
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        if (id == 1) {
            int widgetIndex = buffer.readVarInt();
            int widgetUpdateId = buffer.readVarInt();

            Widget widget = widgets.get(widgetIndex);
            widget.readUpdateInfo(widgetUpdateId, buffer);
        }
    }

    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        if (id == 1) {
            int widgetIndex = buffer.readVarInt();
            int widgetUpdateId = buffer.readVarInt();

            Widget widget = widgets.get(widgetIndex);
            widget.handleClientAction(widgetUpdateId, buffer);
        }
    }

}
