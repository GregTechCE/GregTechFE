package gregtech.api.items.toolitem;

import gregtech.api.items.GTItemGroups;
import gregtech.api.items.GTItemSettings;

public class ToolItemSettings extends GTItemSettings {

    public static final long DEFAULT_ENERGY_PER_DURABILITY_POINT = 100L;
    public static final int DEFAULT_ITEM_DAMAGE_CHANCE = 3;
    public static final float DEFAULT_ATTACK_SPEED = -2.8f;

    public static final int DEFAULT_DAMAGE_BLOCK_BREAK = 1;
    public static final int DEFAULT_DAMAGE_ENTITY_HIT = 1;
    public static final int DEFAULT_DAMAGE_PER_SPECIAL_ACTION = 1;
    public static final int DEFAULT_DAMAGE_PER_CRAFT = 1;

    float miningSpeedMultiplier = 1.0f;
    float attackSpeed = DEFAULT_ATTACK_SPEED;
    float baseAttackDamage = 0.0f;
    float attackDamageMultiplier = 1.0f;

    int damagePerSpecialAction = DEFAULT_DAMAGE_PER_SPECIAL_ACTION;
    int damagePerBlockBreak = DEFAULT_DAMAGE_BLOCK_BREAK;
    int damagePerEntityAttack = DEFAULT_DAMAGE_ENTITY_HIT;
    int damagePerCraft = DEFAULT_DAMAGE_PER_CRAFT;

    float durabilityMultiplier = 1.0f;
    long energyPerDurabilityPoint = DEFAULT_ENERGY_PER_DURABILITY_POINT;
    int itemDamageChance = DEFAULT_ITEM_DAMAGE_CHANCE;

    public ToolItemSettings() {
        this.maxCount(1);
        this.group(GTItemGroups.TOOLS);
    }

    public ToolItemSettings miningSpeedMultiplier(float miningSpeedMultiplier) {
        this.miningSpeedMultiplier = miningSpeedMultiplier;
        return this;
    }

    public ToolItemSettings attackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
        return this;
    }

    public ToolItemSettings baseAttackDamage(float baseAttackDamage) {
        this.baseAttackDamage = baseAttackDamage;
        return this;
    }

    public ToolItemSettings attackDamageMultiplier(float attackDamageMultiplier) {
        this.attackDamageMultiplier = attackDamageMultiplier;
        return this;
    }

    public ToolItemSettings damagePerSpecialAction(int damagePerSpecialAction) {
        this.damagePerSpecialAction = damagePerSpecialAction;
        return this;
    }

    public ToolItemSettings damagePerBlockBreak(int damagePerBlockBreak) {
        this.damagePerBlockBreak = damagePerBlockBreak;
        return this;
    }

    public ToolItemSettings damagePerEntityAttack(int damagePerEntityAttack) {
        this.damagePerEntityAttack = damagePerEntityAttack;
        return this;
    }

    public ToolItemSettings damagePerCraft(int damagePerCraft) {
        this.damagePerCraft = damagePerCraft;
        return this;
    }

    public ToolItemSettings durabilityMultiplier(float durabilityMultiplier) {
        this.durabilityMultiplier = durabilityMultiplier;
        return this;
    }

    public ToolItemSettings energyPerDurabilityPoint(long amount) {
        this.energyPerDurabilityPoint = amount;
        return this;
    }

    public ToolItemSettings itemDamageChance(int itemDamageChance) {
        this.itemDamageChance = itemDamageChance;
        return this;
    }
}
