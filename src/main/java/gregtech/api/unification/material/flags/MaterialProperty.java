package gregtech.api.unification.material.flags;

import com.google.common.base.Preconditions;
import gregtech.api.unification.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class MaterialProperty<T> extends MaterialFlag {

    private final Class<T> propertyValueType;
    private final T defaultValue;

    public MaterialProperty(Settings<T> settings) {
        super(settings);
        Preconditions.checkNotNull(settings.propertyValueType);

        this.propertyValueType = settings.propertyValueType;
        this.defaultValue = settings.defaultValue;
    }

    public T cast(Object data) {
        return propertyValueType.cast(data);
    }

    @Nullable
    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean verifyPropertyValue(Material material, Object propertyValue) {
        if (propertyValue == null) {
            LOGGER.error("Material {} property {} value is null, which is invalid", material, this);
            return false;
        }

        if (!propertyValueType.isInstance(propertyValue)) {
            LOGGER.error("Material {} property {} value is not of a type {} (actual type: {})",
                    material, this, propertyValueType, propertyValue.getClass());
            return false;
        }

        if (propertyValue instanceof VerifiedPropertyValue verifiedValue) {
            return verifiedValue.verifyValue(material, this);
        }
        return true;
    }

    public static class Settings<T> extends MaterialFlag.Settings {

        Class<T> propertyValueType;
        T defaultValue;

        public Settings() {
        }

        public Settings<T> valueType(Class<T> propertyValueType) {
            Preconditions.checkNotNull(propertyValueType);

            this.propertyValueType = propertyValueType;
            return this;
        }

        public Settings<T> defaultValue(T defaultValue) {
            Preconditions.checkNotNull(defaultValue);

            this.defaultValue = defaultValue;
            return this;
        }

        //Override parent methods so they return MaterialProperty.Settings

        @Override
        public Settings<T> requires(MaterialFlag materialFlag) {
            super.requires(materialFlag);
            return this;
        }

        @Override
        public Settings<T> requiresEither(MaterialFlag... flags) {
            super.requiresEither(flags);
            return this;
        }

        @Override
        public <R> Settings<T> requiresPredicate(MaterialProperty<R> property, Predicate<R> valuePredicate) {
            super.requiresPredicate(property, valuePredicate);
            return this;
        }

        @Override
        public <R> Settings<T> requires(MaterialProperty<R> property, R value) {
            super.requires(property, value);
            return this;
        }

        @Override
        public Settings<T> conflictsWith(MaterialFlag materialFlag) {
            super.conflictsWith(materialFlag);
            return this;
        }
    }
}
