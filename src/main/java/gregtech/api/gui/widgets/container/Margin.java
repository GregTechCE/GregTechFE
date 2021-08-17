package gregtech.api.gui.widgets.container;

import gregtech.api.gui.util.Size;

public class Margin {

    private final int top;
    private final int left;
    private final int bottom;
    private final int right;

    public Margin() {
        this(0);
    }

    public Margin(int margin) {
        this(margin, margin, margin, margin);
    }

    public Margin(int vertical, int horizontal) {
        this(vertical, horizontal, vertical, horizontal);
    }

    public Margin(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getBottom() {
        return bottom;
    }

    public int getRight() {
        return right;
    }

    public Size getSize() {
        return new Size(this.left + this.right, this.top + this.bottom);
    }
}
