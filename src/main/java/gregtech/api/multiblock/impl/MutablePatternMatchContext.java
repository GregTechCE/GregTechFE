package gregtech.api.multiblock.impl;

import gregtech.api.multiblock.ContextKey;
import gregtech.api.multiblock.PatternMatchContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

class MutablePatternMatchContext implements PatternMatchContext {

    private final Map<ContextKey<?>, Object> contextData = new HashMap<>();

    public void resetContext() {
        this.contextData.clear();
    }

    @Override
    public <T> void set(ContextKey<T> contextKey, T value) {
        this.contextData.put(contextKey, value);
    }

    @Override
    public <T> Optional<T> get(ContextKey<T> contextKey) {
        Object rawValue = this.contextData.get(contextKey);
        return Optional.ofNullable(contextKey.cast(rawValue));
    }

    @Override
    public <T> T computeIfAbsent(ContextKey<T> contextKey, Supplier<T> constructor) {
        Object rawValue = this.contextData.get(contextKey);
        if (rawValue != null) {
            return contextKey.cast(rawValue);
        }

        T newValue = constructor.get();
        this.contextData.put(contextKey, newValue);
        return newValue;
    }
}
