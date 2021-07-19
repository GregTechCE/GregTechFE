package gregtech.api.block;

import gregtech.api.GTValues;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class GTBlockTags {
    public static Tag.Identified<Block> WRENCH_MINEABLE;

    private static Tag.Identified<Block> register(String name) {
        return (Tag.Identified<Block>) TagRegistry.block(new Identifier(GTValues.COMMON_TAG_NAMESPACE, name));
    }

    static {
        WRENCH_MINEABLE = register("mineable/wrench");
    }
}
