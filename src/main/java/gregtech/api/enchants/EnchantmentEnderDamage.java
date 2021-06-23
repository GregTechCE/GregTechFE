package gregtech.api.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public class EnchantmentEnderDamage extends Enchantment {

    public EnchantmentEnderDamage() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
    }

    @Override
    public int getMinPower(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    private static boolean shouldTriggerOnEntity(Entity target) {
        if (target instanceof EndermanEntity ||
            target instanceof ShulkerEntity ||
            target instanceof EndermiteEntity ||
            target instanceof EnderDragonEntity) {
            return true;
        }

        String entityName = Registry.ENTITY_TYPE.getKey(target.getType())
                .map(key -> key.getValue().getPath())
                .orElseThrow();
        return entityName.contains("ender");
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (shouldTriggerOnEntity(target)) {
            if (target instanceof LivingEntity livingEntity) {
                int effectDuration = level * 200;
                int effectAmplifier = MathHelper.clamp((level * 5) / 7, 0, 2);

                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, effectAmplifier));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, effectAmplifier));
            }
            target.damage(DamageSource.mob(user), level * 2.5f);
        }
    }
}