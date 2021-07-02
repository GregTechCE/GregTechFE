package gregtech.api.multiblock;

import com.google.common.base.Preconditions;
import net.minecraft.util.Identifier;

public class ContextKey<T> {

    private final Identifier name;
    private final Class<T> valueClass;

    public ContextKey(Identifier name, Class<T> valueClass) {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(valueClass, "valueClass");
        this.name = name;
        this.valueClass = valueClass;
    }

    public Identifier getName() {
        return name;
    }

    public T cast(Object value) {
        return this.valueClass.cast(value);
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
