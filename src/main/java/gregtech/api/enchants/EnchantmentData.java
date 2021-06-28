package gregtech.api.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class EnchantmentData {

    private final Enchantment enchantment;
    private final int level;

    public EnchantmentData(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnchantmentData that = (EnchantmentData) o;

        if (level != that.level) return false;
        return enchantment.equals(that.enchantment);
    }

    @Override
    public int hashCode() {
        int result = enchantment.hashCode();
        result = 31 * result + level;
        return result;
    }

    public static Map<Enchantment, Integer> reduceEnchantmentList(List<EnchantmentData> enchantments) {
        BinaryOperator<EnchantmentData> enchantmentCombiner = (data1, data2) -> {
            Enchantment enchantment = data1.getEnchantment();
            int level = data1.getLevel() + data2.getLevel();

            int maxLevel = enchantment.getMaxLevel();
            int minLevel = enchantment.getMinLevel();
            int clampedLevel = MathHelper.clamp(level, minLevel, maxLevel);

            return new EnchantmentData(enchantment, clampedLevel);
        };

        Collector<EnchantmentData, EnchantmentData, Integer> resultCollector = Collectors.collectingAndThen(
                Collectors.reducing(enchantmentCombiner),
                optional -> optional.orElseThrow().getLevel());

        return enchantments.stream()
                .collect(Collectors.groupingBy(EnchantmentData::getEnchantment, resultCollector));
    }
}
