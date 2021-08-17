package gregtech.api.capability.internal;

import com.google.common.base.Preconditions;

import java.util.*;

public class MultiblockAbilityList {

    private final Map<MultiblockAbility<?>, List<Object>> abilityMap = new HashMap<>();

    public <T> void add(MultiblockAbility<T> abilityType, T ability) {
        Preconditions.checkNotNull(abilityType, "abilityType");
        Preconditions.checkNotNull(ability, "ability");

        List<Object> attributeValues = this.abilityMap.computeIfAbsent(abilityType, k -> new ArrayList<>());
        attributeValues.add(abilityType.cast(ability));
    }

    public boolean hasAbility(MultiblockAbility<?> abilityType) {
        Preconditions.checkNotNull(abilityType, "abilityType");
        return this.abilityMap.containsKey(abilityType);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAbility(MultiblockAbility<T> abilityType) {
        Preconditions.checkNotNull(abilityType, "abilityType");

        List<Object> attributeValues = this.abilityMap.getOrDefault(abilityType, Collections.emptyList());
        return abilityType.combine((List<T>) attributeValues);
    }
}
