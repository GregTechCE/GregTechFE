package gregtech.api.gui.widgets.group;

import gregtech.api.gui.util.GuiUtils;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.ScissorStack;
import gregtech.api.gui.util.Size;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ScrollableListWidget extends AbstractWidgetGroup {

    protected int totalListHeight;
    protected int slotHeight;
    protected int scrollOffset;
    protected int scrollPaneWidth = 10;
    protected int lastMouseX;
    protected int lastMouseY;
    protected boolean draggedOnScrollBar;

    public ScrollableListWidget(int xPosition, int yPosition, int width, int height) {
        super(new Position(xPosition, yPosition), new Size(width, height));
    }

    @Override
    public void addWidget(Widget widget) {
        super.addWidget(widget);
    }

    @Override
    protected boolean recomputeSize() {
        updateElementPositions();
        return false;
    }

    private void addScrollOffset(int offset) {
        this.scrollOffset = MathHelper.clamp(scrollOffset + offset, 0, totalListHeight - getSize().height);
        updateElementPositions();
    }

    private boolean isOnScrollPane(int mouseX, int mouseY) {
        Position pos = getPosition();
        Size size = getSize();
        return isMouseOver(pos.x + size.width - scrollPaneWidth, pos.y, scrollPaneWidth, size.height, mouseX, mouseY);
    }

    @Override
    protected void onPositionUpdate() {
        updateElementPositions();
    }

    private void updateElementPositions() {
        Position position = getPosition();
        int currentPosY = position.y - scrollOffset;
        int totalListHeight = 0;
        for (Widget widget : widgets) {
            Position childPosition = new Position(position.x, currentPosY);
            widget.setParentPosition(childPosition);
            currentPosY += widget.getSize().getHeight();
            totalListHeight += widget.getSize().getHeight();
            final Size size = getSize();
            widget.applyScissor(position.x, position.y, size.width - scrollPaneWidth, size.height);
        }
        this.totalListHeight = totalListHeight;
        this.slotHeight = widgets.isEmpty() ? 0 : totalListHeight / widgets.size();
    }

    @Override
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY, RenderContext renderContext) {
        //make sure mouse is not hovered on any element when outside of bounds
        if (!isPositionInsideScissor(mouseX, mouseY)) {
            mouseX = Integer.MAX_VALUE;
            mouseY = Integer.MAX_VALUE;
        }

        super.drawInForeground(matrices, mouseX, mouseY, renderContext);
    }

    @Override
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        //make sure mouse is not hovered on any element when outside of bounds
        if (!isPositionInsideScissor(mouseX, mouseY)) {
            mouseX = Integer.MAX_VALUE;
            mouseY = Integer.MAX_VALUE;
        }

        int finalMouseX = mouseX;
        int finalMouseY = mouseY;
        Position position = getPosition();
        Size size = getSize();

        int paneSize = scrollPaneWidth;
        int scrollX = position.x + size.width - paneSize;

        GuiUtils.drawSolidRect(matrices, scrollX, position.y, paneSize, size.height, 0xFF666666, 0);
        GuiUtils.drawSolidRect(matrices, scrollX + 1, position.y + 1, paneSize - 2, size.height - 2, 0xFF888888, 0);

        int maxScrollOffset = totalListHeight - getSize().height;
        float scrollPercent = maxScrollOffset == 0 ? 0 : scrollOffset / (maxScrollOffset * 1.0f);
        int scrollSliderHeight = 14;
        int scrollSliderY = Math.round(position.y + (size.height - scrollSliderHeight) * scrollPercent);

        GuiUtils.drawGradientRect(matrices, scrollX + 1, scrollSliderY, paneSize - 2, scrollSliderHeight, 0xFF555555, 0xFF454545, 0);

        ScissorStack.useScissor(position.x, position.y, size.width - paneSize, size.height, () ->
            super.drawInBackground(matrices, finalMouseX, finalMouseY, deltaTicks, renderContext));
    }

    @Override
    public boolean isWidgetClickable(final Widget widget) {
        if (!super.isWidgetClickable(widget)) {
            return false;
        }
        return isWidgetOverlapsScissor(widget);
    }

    private boolean isPositionInsideScissor(int mouseX, int mouseY) {
        return isMouseOverElement(mouseX, mouseY) && !isOnScrollPane(mouseX, mouseY);
    }

    private boolean isWidgetOverlapsScissor(Widget widget) {
        final Position position = widget.getPosition();
        final Size size = widget.getSize();
        final int x0 = position.x;
        final int y0 = position.y;
        final int x1 = position.x + size.width - 1;
        final int y1 = position.y + size.height - 1;
        return isPositionInsideScissor(x0, y0) ||
               isPositionInsideScissor(x0, y1) ||
               isPositionInsideScissor(x1, y0) ||
               isPositionInsideScissor(x1, y1);
    }

    private boolean isBoxInsideScissor(Rectangle rectangle) {
        return isPositionInsideScissor(rectangle.x, rectangle.y) &&
            isPositionInsideScissor(rectangle.x + rectangle.width - 1, rectangle.y + rectangle.height - 1);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (isMouseOverElement(mouseX, mouseY, true)) {
            int direction = -MathHelper.clamp(amount, -1, 1);
            int moveDelta = direction * (slotHeight / 2);
            addScrollOffset(moveDelta);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
        if (isOnScrollPane(mouseX, mouseY)) {
            this.draggedOnScrollBar = true;
        }
        if (isPositionInsideScissor(mouseX, mouseY)) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int mouseDelta = (mouseY - lastMouseY);
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
        if (draggedOnScrollBar) {
            addScrollOffset(mouseDelta);
            return true;
        }
        if (isPositionInsideScissor(mouseX, mouseY)) {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.draggedOnScrollBar = false;
        if (isPositionInsideScissor(mouseX, mouseY)) {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public Object getIngredientOverMouse(int mouseX, int mouseY) {
        if (isPositionInsideScissor(mouseX, mouseY)) {
            return super.getIngredientOverMouse(mouseX, mouseY);
        }
        return null;
    }

    @Override
    public List<Target> getPhantomTargets(Object ingredient) {
        //for phantom targets, show only ones who are fully inside scissor box to avoid visual glitches
        return super.getPhantomTargets(ingredient).stream()
            .filter(it -> isBoxInsideScissor(it.getArea()))
            .collect(Collectors.toList());
    }
}
