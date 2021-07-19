package gregtech.api.render.model.state;

import com.google.common.collect.ImmutableMap;
import net.minecraft.state.property.Property;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ModelState<S> {

    private static final Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_MAP_PRINTER = entry -> {
        Property<?> property = entry.getKey();
        String name = property.getName();
        return name + "=" + nameProperty(property, entry.getValue());
    };
    protected final S owner;
    private final ImmutableMap<Property<?>, Comparable<?>> entries;

    ModelState(S owner, ImmutableMap<Property<?>, Comparable<?>> entries) {
        this.owner = owner;
        this.entries = entries;
    }

    public S getOwner() {
        return owner;
    }

    public ImmutableMap<Property<?>, Comparable<?>> getEntries() {
        return entries;
    }

    public Collection<Property<?>> getProperties() {
        return Collections.unmodifiableCollection(this.entries.keySet());
    }

    public <T extends Comparable<T>> boolean contains(Property<T> property) {
        return this.entries.containsKey(property);
    }

    public <T extends Comparable<T>> T get(Property<T> property) {
        Comparable<?> comparable = this.entries.get(property);
        if (comparable == null) {
            throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.owner);
        } else {
            return property.getType().cast(comparable);
        }
    }

    public <T extends Comparable<T>> Optional<T> getOrEmpty(Property<T> property) {
        Comparable<?> comparable = this.entries.get(property);
        return comparable == null ? Optional.empty() : Optional.of(property.getType().cast(comparable));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelState<?> that = (ModelState<?>) o;
        return owner.equals(that.owner) && entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, entries);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.owner);
        if (!this.getEntries().isEmpty()) {
            stringBuilder.append('[');
            stringBuilder.append((String)this.getEntries().entrySet().stream().map(PROPERTY_MAP_PRINTER)
                    .collect(Collectors.joining(",")));
            stringBuilder.append(']');
        }

        return stringBuilder.toString();
    }

    private static <T extends Comparable<T>> String nameProperty(Property<T> property, Object value) {
        return property.name(property.getType().cast(value));
    }

    public static class Builder<S> {
        private final S owner;
        private final Collection<Property<?>> properties;
        private final ImmutableMap.Builder<Property<?>, Comparable<?>> propertyValues;

        public Builder(ModelStateManager<S> stateManager, ModelState<S> defaultState) {
            this.owner = stateManager.getOwner();
            this.properties = stateManager.getProperties();
            this.propertyValues = ImmutableMap.builder();
            this.propertyValues.putAll(defaultState.getEntries());
        }

        public Builder(ModelStateManager<S> stateManager) {
            this(stateManager, stateManager.getDefaultState());
        }

        public <T extends Comparable<T>> Builder<S> with(Property<T> property, T value) {
            if (!this.properties.contains(property)) {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.owner);
            }
            this.propertyValues.put(property, property.getType().cast(value));
            return this;
        }

        public ModelState<S> build() {
            return new ModelState<>(this.owner, this.propertyValues.build());
        }
    }
}
