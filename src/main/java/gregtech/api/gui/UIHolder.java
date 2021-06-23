package gregtech.api.gui;

import gregtech.api.util.IDirtyNotifiable;

public interface UIHolder extends IDirtyNotifiable {

    boolean isValid();

    boolean isClient();

    void markDirty();

}
