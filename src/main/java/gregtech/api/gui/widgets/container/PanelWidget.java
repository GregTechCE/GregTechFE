package gregtech.api.gui.widgets.container;

import gregtech.api.gui.Widget;
import gregtech.api.gui.util.NullWidget;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PanelWidget extends Widget {

    protected void layoutVerticalBox(List<PanelSlot> slots, Position parentPos, Size parentSize) {
        ArrayList<PanelSlot> topSlots = new ArrayList<>();
        ArrayList<PanelSlot> bottomSlots = new ArrayList<>();

        ArrayList<PanelSlot> centerSlots = new ArrayList<>();
        ArrayList<PanelSlot> fillSlots = new ArrayList<>();

        //categorize widgets first
        for (PanelSlot panelSlot : slots) {
            switch (panelSlot.verticalAlignment) {
                case TOP -> topSlots.add(panelSlot);
                case BOTTOM -> bottomSlots.add(panelSlot);
                case CENTER -> centerSlots.add(panelSlot);
                case FILL -> fillSlots.add(panelSlot);
            }
        }

        //layout top-aligned widgets
        int topOffsetY = 0;
        for (PanelSlot topSlot : topSlots) {
            Size widgetSize = topSlot.getPreferredSizeWithMargin();
            alignWidgetHorizontally(topSlot, parentPos, parentSize, topOffsetY, widgetSize.height);
            topOffsetY += widgetSize.height;
        }

        //layout bottom-aligned widgets
        int bottomOffsetY = 0;
        for (PanelSlot bottomSlot : bottomSlots) {
            Size widgetSize = bottomSlot.getPreferredSizeWithMargin();
            int topY = parentSize.height - widgetSize.height - bottomOffsetY;
            alignWidgetHorizontally(bottomSlot, parentPos, parentSize, topY, widgetSize.height);
            bottomOffsetY += widgetSize.height;
        }

        //layout centered widgets
        int totalCenteredWidgetHeight = 0;
        for (PanelSlot centerSlot : centerSlots) {
            totalCenteredWidgetHeight += centerSlot.getPreferredSizeWithMargin().height;
        }

        int centerPosY = parentSize.height / 2;

    }

    protected static void alignWidgetHorizontally(PanelSlot slot, Position parentPos, Size parentSize, int resultPosY, int resultHeight) {
        Widget widget = slot.getContents();
        Size widgetSize = widget.getPreferredSize();
        Size widgetSizeWithMargin = widgetSize.add(slot.getMargin().getSize());

        int resultPosX = 0;
        int resultWidth = widgetSizeWithMargin.width;

        switch (slot.getHorizontalAlignment()) {
            case LEFT -> {
                resultPosX = 0;
                resultWidth = Math.min(widgetSizeWithMargin.width, parentSize.width);
            }
            case RIGHT -> {
                resultPosX = Math.max(0, parentSize.width - widgetSizeWithMargin.width);
                resultWidth = parentSize.width - resultPosX;
            }
            case CENTER -> {
                int cappedWidth = Math.min(parentSize.width, widgetSizeWithMargin.width);
                resultPosX = parentSize.width / 2 - cappedWidth / 2;
                resultWidth = cappedWidth;
            }
            case FILL -> {
                resultPosX = 0;
                resultWidth = parentSize.width;
            }
        }

        Size actualWidgetSize = new Size(resultWidth, resultHeight);
        Pair<Position, Size> finalWidgetPos = layoutWidgetInMargin(actualWidgetSize, widgetSize, slot.getMargin());

        Position finalPosition = new Position(
                resultPosX + finalWidgetPos.getLeft().getX(),
                resultPosY + finalWidgetPos.getLeft().getY());

        widget.setPosition(finalPosition.add(parentPos));
        widget.setActualSize(finalWidgetPos.getRight());
        widget.onWidgetLayoutChanged();
    }

    protected static Pair<Position, Size> layoutWidgetInMargin(Size actualWidgetSize, Size preferredWidgetSize, Margin margin) {
        Size marginTotalSize = margin.getSize();
        int spaceForHorizontalMargin = Math.max(0, actualWidgetSize.width - preferredWidgetSize.width);
        int spaceForVerticalMargin = Math.max(0, actualWidgetSize.height - preferredWidgetSize.height);

        float horizontalScale = spaceForHorizontalMargin / (1.0f * marginTotalSize.width);
        float verticalScale = spaceForVerticalMargin / (1.0f * marginTotalSize.height);

        int scaledLeft = Math.round(margin.getLeft() * horizontalScale);
        int scaledRight = Math.round(margin.getRight() * horizontalScale);

        int scaledTop = Math.round(margin.getTop() * verticalScale);
        int scaledBottom = Math.round(margin.getBottom() * verticalScale);

        Position position = new Position(scaledLeft, scaledTop);
        Size size = new Size(actualWidgetSize.width - scaledRight, actualWidgetSize.height - scaledBottom);
        return Pair.of(position, size);
    }

    public static class PanelSlot {

        private Margin margin = new Margin();
        private VerticalAlignment verticalAlignment = VerticalAlignment.FILL;
        private HorizontalAlignment horizontalAlignment = HorizontalAlignment.FILL;
        private float fillPercent = 1.0f;
        private Widget contents = NullWidget.INSTANCE;

        public PanelSlot margin(Margin margin) {
            this.margin = margin;
            return this;
        }

        public PanelSlot HAlign(HorizontalAlignment horizontalAlignment) {
            this.horizontalAlignment = horizontalAlignment;
            return this;
        }

        public PanelSlot VAlign(VerticalAlignment verticalAlignment) {
            this.verticalAlignment = verticalAlignment;
            return this;
        }

        public PanelSlot fillPercent(float fillPercent) {
            this.fillPercent = fillPercent;
            return this;
        }

        public PanelSlot contents(Widget contents) {
            this.contents = contents;
            return this;
        }

        public Margin getMargin() {
            return margin;
        }

        public VerticalAlignment getVerticalAlignment() {
            return verticalAlignment;
        }

        public HorizontalAlignment getHorizontalAlignment() {
            return horizontalAlignment;
        }

        public float getFillPercent() {
            return fillPercent;
        }

        public Widget getContents() {
            return contents;
        }

        public Size getPreferredSizeWithMargin() {
            return this.contents.getPreferredSize().add(this.margin.getSize());
        }
    }
}
