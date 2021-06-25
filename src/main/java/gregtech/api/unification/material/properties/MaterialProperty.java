package gregtech.api.unification.material.properties;

import gregtech.api.GTValues;
import gregtech.api.unification.material.flags.MaterialFlag;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MaterialProperty<T> {
    private final Class<T> propertyValueType;
    private final Set<MaterialFlag> requiredFlags = new HashSet<>();
    private final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();

    private MaterialProperty(Class<T> propertyValueType, Set<MaterialProperty<?>> requiredProperties, Set<MaterialFlag> requiredFlags) {
        this.propertyValueType = propertyValueType;
        this.requiredFlags.addAll(requiredFlags);
        this.requiredProperties.addAll(requiredProperties);
    }

    public T cast(Object data) {
        return propertyValueType.cast(data);
    }

    public Set<MaterialFlag> getRequiredFlags() {
        return Collections.unmodifiableSet(requiredFlags);
    }

    @SuppressWarnings("java:S1452")
    public Set<MaterialProperty<?>> getRequiredProperties() {
        return Collections.unmodifiableSet(requiredProperties);
    }


    public static class Builder<T> {
        @SuppressWarnings("unchecked")
        public static final Registry<MaterialProperty<?>> REGISTRY =
                FabricRegistryBuilder.createSimple((Class<MaterialProperty<?>>) (Object) MaterialProperty.class,
                        new Identifier(GTValues.MODID, "material_property"))
                        .buildAndRegister();


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
            var materialProperty = new MaterialProperty<>(propertyValueType, requiredProperties, requiredFlags);

            return Registry.register(REGISTRY, new Identifier(GTValues.MODID, name), materialProperty);
        }
    }
}
