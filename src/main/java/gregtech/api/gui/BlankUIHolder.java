package gregtech.api.gui;

public class BlankUIHolder implements UIHolder {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public void markDirty() {
    }
}