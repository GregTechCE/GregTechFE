package gregtech.api.util.ref;

import alexiil.mc.lib.attributes.misc.Reference;

public class SimpleReference<T> implements Reference<T> {

    public T value;

    public SimpleReference(T initialValue) {
        this.value = initialValue;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean set(T value) {
        this.value = value;
        return true;
    }

    @Override
    public boolean isValid(T value) {
        return true;
    }
}
