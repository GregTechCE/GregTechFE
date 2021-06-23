package gregtech.api.gui.igredient;

import java.awt.*;
import java.util.List;

public interface IGhostIngredientTarget {

    List<Target> getPhantomTargets(Object ingredient);

    interface Target {
        Rectangle getArea();

        boolean accept(Object ingredient);
    }
}
