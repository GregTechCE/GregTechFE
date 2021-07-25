package gregtech.api.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GTGameRules {

    public static GameRules.Key<GameRules.BooleanRule> DO_OVERVOLTAGE_EXPLOSIONS;

    public static void ensureInitialized() {
    }

    static {
        DO_OVERVOLTAGE_EXPLOSIONS = GameRuleRegistry.register("doOvervoltageExplosions", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    }
}
