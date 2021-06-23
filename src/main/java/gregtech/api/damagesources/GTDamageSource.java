package gregtech.api.damagesources;

import net.minecraft.entity.damage.DamageSource;

public class GTDamageSource extends DamageSource {

    private static final DamageSource EXPLOSION = new GTDamageSource("explosion").setExplosive();
    private static final DamageSource HEAT = new GTDamageSource("heat").setBypassesArmor();
    private static final DamageSource FROST = new GTDamageSource("frost").setBypassesArmor();
    private static final DamageSource ELECTRIC = new GTDamageSource("electric").setBypassesArmor();
    private static final DamageSource RADIATION = new GTDamageSource("radiation").setBypassesArmor();
    private static final DamageSource TURBINE = new GTDamageSource("turbine");
    private static final DamageSource CRUSHER = new GTDamageSource("crusher");

    protected GTDamageSource(String name) {
        super(name);
    }
}