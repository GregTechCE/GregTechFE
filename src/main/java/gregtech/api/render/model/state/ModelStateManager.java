package gregtech.api.render.model.state;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModelStateManager<S> {

    public static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");

    private final S owner;
    private final ImmutableSortedMap<String, Property<?>> properties;
    private final ModelState<S> defaultState;

    protected ModelStateManager(S owner, Map<String, Property<?>> propertiesMap, Map<Property<?>, Comparable<?>> defaultValueOverrides) {
        this.owner = owner;
        this.properties = ImmutableSortedMap.copyOf(propertiesMap);
        this.defaultState = buildDefaultState(defaultValueOverrides);

        this.properties.values().forEach(this::validateProperty);
    }

    private ModelState<S> buildDefaultState(Map<Property<?>, Comparable<?>> defaultValueOverrides) {
        ImmutableMap.Builder<Property<?>, Comparable<?>> propertyValues = ImmutableMap.builder();

        for (Property<?> property : properties.values()) {
            propertyValues.put(property, property.getValues().iterator().next());
        }

        propertyValues.putAll(defaultValueOverrides);
        return new ModelState<>(this.owner, propertyValues.build());
    }

    public ModelState<S> getDefaultState() {
        return this.defaultState;
    }

    public S getOwner() {
        return this.owner;
    }

    @Nullable
    public Property<?> getProperty(String name) {
        return this.properties.get(name);
    }

    public Collection<Property<?>> getProperties() {
        return this.properties.values();
    }

    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("block", this.owner)
                .add("properties", this.properties.values()
                        .stream()
                        .map(Property::getName)
                        .collect(Collectors.toList()))
                .toString();
    }

    private <T extends Comparable<T>> void validateProperty(Property<T> property) {
        String propertyName = property.getName();
        if (!VALID_NAME_PATTERN.matcher(propertyName).matches()) {
            throw new IllegalArgumentException(this.owner + " has invalidly named property: " + propertyName);
        }

        Collection<T> propertyValues = property.getValues();
        if (propertyValues.size() <= 1) {
            throw new IllegalArgumentException(this.owner + " attempted use property " + propertyName + " with <= 1 possible values");
        }

        for (T value : propertyValues) {
            String propertyValueName = property.name(value);

            if (!VALID_NAME_PATTERN.matcher(propertyValueName).matches()) {
                throw new IllegalArgumentException(this.owner + " has property: " + propertyName + " with invalidly named value: " + propertyValueName);
            }
        }
    }
}
