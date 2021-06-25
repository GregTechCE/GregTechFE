package gregtech.api.unification.material.properties;

import gregtech.api.unification.material.flags.MaterialFlag;
import gregtech.api.util.registry.AlreadyRegisteredKeyException;
import gregtech.api.util.registry.GTRegistry;
import gregtech.api.util.registry.GTRegistryKey;

import java.util.HashSet;
import java.util.Set;

public class MaterialProperty<T> implements GTRegistryKey {
    private final String name;
    private final Class<T> propertyValueType;
    private final Set<MaterialFlag> requiredFlags = new HashSet<>();
    private final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

    private MaterialProperty(String name, Class<T> propertyValueType, Set<MaterialProperty<?>> requiredProperties, Set<MaterialFlag> requiredFlags) {
        this.name = name;
        this.propertyValueType = propertyValueType;
        this.requiredFlags.addAll(requiredFlags);
        this.requiredProperties.addAll(requiredProperties);
    }

    public T cast(Object data) {
        return propertyValueType.cast(data);
    }

    @Override
    public String getKey() {
        return name;
    }


    public static class Builder<T> {
        private static final GTRegistry<MaterialProperty<?>> registry = new GTRegistry<>();

        private final String name;
        private final Class<T> propertyValueType;

        private final Set<MaterialFlag> requiredFlags = new HashSet<>();
        private final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

        public Builder(String name, Class<T> propertyValueType) {
            this.name = name;
            this.propertyValueType = propertyValueType;
        }

        public MaterialProperty.Builder<T> requires(MaterialProperty<?> property) {
            this.requiredProperties.add(property);
            return this;
        }

        public MaterialProperty.Builder<T> requires(MaterialFlag materialFlag) {
            this.requiredFlags.add(materialFlag);
            return this;
        }

        public MaterialProperty<T> build() {
            var materialProperty = new MaterialProperty<>(name, propertyValueType, requiredProperties, requiredFlags);

            try {
                registry.put(materialProperty);
            } catch (AlreadyRegisteredKeyException e) {
                //TODO: Log
                return null;
            }

            return materialProperty;
        }
    }
}
