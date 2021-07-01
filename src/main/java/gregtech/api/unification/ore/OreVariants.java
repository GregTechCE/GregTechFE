package gregtech.api.unification.ore;

import gregtech.api.GTValues;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class OreVariants {

    public static final OreVariant STONE;
    public static final OreVariant DEEPSLATE;

    public static final OreVariant BEDROCK;
    public static final OreVariant GRAVEL;
    public static final OreVariant SANDSTONE;

    public static final OreVariant NETHERRACK;
    public static final OreVariant BASALT;

    public static final OreVariant END_STONE;


    private static OreVariant register(String name, OreVariant variant) {
        return Registry.register(OreVariant.REGISTRY, new Identifier(GTValues.MODID, name), variant);
    }

    static {
        //...
    }
}
