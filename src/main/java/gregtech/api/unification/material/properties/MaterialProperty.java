package gregtech.api.unification.material.properties;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import gregtech.api.GTValues;
import gregtech.api.unification.material.flags.MaterialFlag;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class MaterialProperty<T> {

    @SuppressWarnings("unchecked")
    public static final Registry<MaterialProperty<?>> REGISTRY = FabricRegistryBuilder
            .createSimple((Class<MaterialProperty<?>>) (Object) MaterialProperty.class,
                    new Identifier(GTValues.MODID, "material_property"))
            .buildAndRegister();

    private final Class<T> propertyValueType;
    private final Set<MaterialFlag> requiredFlags;
    private final Set<MaterialProperty<?>> requiredProperties;
    private final T defaultValue;

    public MaterialProperty(Settings<T> settings) {
        Preconditions.checkNotNull(settings.propertyValueType);

        this.propertyValueType = settings.propertyValueType;
        this.defaultValue = settings.defaultValue;
        this.requiredFlags = ImmutableSet.copyOf(settings.requiredFlags);
        this.requiredProperties = ImmutableSet.copyOf(settings.requiredProperties);
    }

    public T cast(Object data) {
        return propertyValueType.cast(data);
    }

    public Set<MaterialFlag> getRequiredFlags() {
        return requiredFlags;
    }

    public Set<MaterialProperty<?>> getRequiredProperties() {
        return requiredProperties;
    }

    @Nullable
    public T getDefaultValue() {
        return defaultValue;
    }

    public static class Settings<T> {

        Class<T> propertyValueType;
        T defaultValue;
        final Set<MaterialFlag> requiredFlags = new HashSet<>();
        final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

        public Settings() {
        }

        public Settings<T> valueType(Class<T> propertyValueType) {
            this.propertyValueType = propertyValueType;
            return this;
        }

        public Settings<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Settings<T> requires(MaterialProperty<?> property) {
            this.requiredProperties.add(property);
            return this;
        }

        public Settings<T> requires(MaterialFlag materialFlag) {
            this.requiredFlags.add(materialFlag);
            return this;
        }
    }
}
