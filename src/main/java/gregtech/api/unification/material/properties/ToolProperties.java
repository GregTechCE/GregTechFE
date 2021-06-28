package gregtech.api.unification.material.properties;

import com.google.common.collect.ImmutableList;
import gregtech.api.enchants.EnchantmentData;

import java.util.Arrays;
import java.util.List;

public class ToolProperties {

    private final float miningSpeed;
    private final float attackDamage;
    private final int durability;
    private final int enchantability;
    private final List<EnchantmentData> enchantments;

    private ToolProperties(float miningSpeed, float attackDamage, int durability, int enchantability, List<EnchantmentData> enchantments) {
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.durability = durability;
        this.enchantability = enchantability;
        this.enchantments = ImmutableList.copyOf(enchantments);
    }

    public static ToolProperties create(float miningSpeed, float attackDamage, int durability, int enchantability, EnchantmentData... enchantments) {
        return new ToolProperties(miningSpeed, attackDamage, durability, enchantability, Arrays.asList(enchantments));
    }

    public float getMiningSpeed() {
        return miningSpeed;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public int getDurability() {
        return durability;
    }

    public int getEnchantability() {
        return enchantability;
    }

    public List<EnchantmentData> getEnchantments() {
        return enchantments;
    }
}
