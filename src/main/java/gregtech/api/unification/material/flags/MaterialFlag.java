package gregtech.api.unification.material.flags;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MaterialFlag {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MaterialFlag.class);

    public static final Registry<MaterialFlag> REGISTRY = FabricRegistryBuilder
            .createSimple(MaterialFlag.class, new Identifier(GTValues.MODID, "material_flags"))
            .buildAndRegister();

    private final Set<MaterialFlag> requiredFlags;
    private final Set<List<MaterialFlag>> requiredEitherFlags;
    private final Set<MaterialFlag> conflictingFlags;
    private final Map<MaterialProperty<?>, Predicate<?>> requiredProperties;

    public MaterialFlag(Settings settings) {
        this.conflictingFlags = ImmutableSet.copyOf(settings.conflictingFlags);
        this.requiredFlags = ImmutableSet.copyOf(settings.requiredFlags);
        this.requiredEitherFlags = ImmutableSet.copyOf(settings.requiredEitherFlags);
        this.requiredProperties = ImmutableMap.copyOf(settings.requiredProperties);
        verifyConflictingFlags();
    }

    public boolean verifyMaterialFlags(Material material) {
        boolean isMaterialValid = true;

        for (MaterialFlag conflictingFlag : conflictingFlags) {
            if (material.hasFlag(conflictingFlag)) {
                LOGGER.error("Material {} has flag {}, which conflicts with flag {}", material, this, conflictingFlag);
                isMaterialValid = false;
            }
        }

        for (MaterialFlag requiredFlag : requiredFlags) {
            if (!material.hasFlag(requiredFlag)) {
                LOGGER.error("Material {} has flag {}, which requires flag {} to be also set", material, this, requiredFlag);
                isMaterialValid = false;
            }
        }

        for (List<MaterialFlag> eitherFlag : requiredEitherFlags) {
            boolean foundAnyFlag = false;

            for (MaterialFlag flag : eitherFlag) {
                foundAnyFlag |= material.hasFlag(flag);
            }
            if (!foundAnyFlag) {
                LOGGER.error("Material {} has flag {}, which requires either of flags {} to be set", material, this, eitherFlag);
                isMaterialValid = false;
            }
        }

        for (MaterialProperty<?> requiredProperty : requiredProperties.keySet()) {
            @SuppressWarnings("unchecked")
            Predicate<Object> valuePredicate = (Predicate<Object>) requiredProperties.get(requiredProperty);
            @SuppressWarnings("unchecked")
            Optional<Object> propertyValue = (Optional<Object>) material.queryProperty(requiredProperty);

            if (propertyValue.isPresent() && !valuePredicate.test(propertyValue.get())) {
                LOGGER.error("Material {} has value {} of property {}, which does not match flag {} predicate",
                        material, propertyValue.get(), requiredProperty, this);
                isMaterialValid = true;
            }
        }

        return isMaterialValid;
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
        final Set<List<MaterialFlag>> requiredEitherFlags = new HashSet<>();
        final Set<MaterialFlag> conflictingFlags = new HashSet<>();
        final Map<MaterialProperty<?>, Predicate<?>> requiredProperties = new HashMap<>();

        public Settings requires(MaterialFlag materialFlag) {
            this.requiredFlags.add(materialFlag);
            return this;
        }

        public <T> Settings requiresPredicate(MaterialProperty<T> property, Predicate<T> valuePredicate) {
            Preconditions.checkNotNull(property);
            Preconditions.checkNotNull(valuePredicate);

            this.requiredFlags.add(property);
            this.requiredProperties.put(property, valuePredicate);
            return this;
        }

        public <T> Settings requires(MaterialProperty<T> property, T value) {
            return this.requiresPredicate(property, Predicate.isEqual(value));
        }

        public Settings requiresEither(MaterialFlag... flags) {
            Preconditions.checkArgument(flags.length >= 2);
            for (MaterialFlag flag : flags) {
                Preconditions.checkNotNull(flag, "flag");
            }
            this.requiredEitherFlags.add(Arrays.asList(flags));
            return this;
        }

        public Settings conflictsWith(MaterialFlag materialFlag) {
            Preconditions.checkNotNull(materialFlag);

            this.conflictingFlags.add(materialFlag);
            return this;
        }
    }
}
