package gregtech.api.gui.widgets.group;

import gregtech.api.gui.Widget;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;

public class WidgetGroup extends AbstractWidgetGroup {

    public WidgetGroup() {
        this(Position.ORIGIN);
    }

    public WidgetGroup(Position position) {
        super(position);
    }

    public WidgetGroup(Position position, Size size) {
        super(position, size);
    }

    @Override
    public void addWidget(Widget widget) {
        super.addWidget(widget);
    }
}
