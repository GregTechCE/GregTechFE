package gregtech.api.gui.util;

import gregtech.api.gui.Widget;

public final class NullWidget extends Widget {

    public static Widget INSTANCE = new NullWidget();

    private NullWidget() {
        super(Position.ORIGIN, Size.ZERO);
    }
}
