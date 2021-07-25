package gregtech.api.enchantment;

import gregtech.api.GTValues;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GTEnchantments {

    public static Enchantment DISJUNCTION;

    private static Enchantment register(Identifier id, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, id, enchantment);
    }

    public static void init() {
        DISJUNCTION = register(new Identifier(GTValues.MODID, "disjunction"), new EnchantmentEnderDamage());
    }
}
