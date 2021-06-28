package gregtech.api.unification.material.flags;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import gregtech.api.GTValues;
import gregtech.api.unification.material.properties.MaterialProperty;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.stream.Collectors;

public class MaterialFlag {

    public static final Registry<MaterialFlag> REGISTRY = FabricRegistryBuilder
            .createSimple(MaterialFlag.class, new Identifier(GTValues.MODID, "material_flags"))
            .buildAndRegister();

    private final Set<MaterialFlag> requiredFlags;
    private final Set<MaterialFlag> conflictingFlags;
    private final Set<MaterialProperty<?>> requiredProperties;

    public MaterialFlag(Settings settings) {
        this.conflictingFlags = ImmutableSet.copyOf(settings.conflictingFlags);
        this.requiredFlags = ImmutableSet.copyOf(settings.requiredFlags);
        this.requiredProperties = ImmutableSet.copyOf(settings.requiredProperties);

        verifyConflictingFlags();
    }

    public Set<MaterialFlag> getRequiredFlags() {
        return requiredFlags;
    }

    public Set<MaterialFlag> getConflictingFlags() {
        return conflictingFlags;
    }

    @SuppressWarnings("java:S1452")
    public Set<MaterialProperty<?>> getRequiredProperties() {
        return requiredProperties;
    }

    public Identifier getName() {
        return Preconditions.checkNotNull(REGISTRY.getId(this));
    }

    @Override
    public String toString() {
        return String.valueOf(REGISTRY.getId(this));
    }

    private void verifyConflictingFlags() {
        List<MaterialFlag> conflictingFlags = requiredFlags.stream()
                .filter(this.conflictingFlags::contains)
                .collect(Collectors.toList());
        if (!conflictingFlags.isEmpty()) {
            throw new IllegalArgumentException("Some flags are both required and conflicted with: " + conflictingFlags);
        }
    }

    public static class Settings {
        final Set<MaterialFlag> requiredFlags = new HashSet<>();
        final Set<MaterialFlag> conflictingFlags = new HashSet<>();
        final Set<MaterialProperty<?>> requiredProperties = new HashSet<>();
        Items

        public Settings requires(MaterialFlag materialFlag) {
            this.requiredFlags.add(materialFlag);
            return this;
        }

        public Settings conflictsWith(MaterialFlag materialFlag) {
            this.conflictingFlags.add(materialFlag);
            return this;
        }

        public Settings requires(MaterialProperty<?> property) {
            this.requiredProperties.add(property);
            return this;
        }
    }
}
