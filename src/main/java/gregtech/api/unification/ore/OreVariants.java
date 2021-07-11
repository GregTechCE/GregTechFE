package gregtech.api.unification.ore;

import gregtech.api.GTValues;
import net.minecraft.block.Blocks;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class OreVariants {

    public static final OreVariant STONE;
    public static final OreVariant DEEPSLATE;

    public static final OreVariant BEDROCK;
    public static final OreVariant GRAVEL;
    public static final OreVariant SANDSTONE;

    public static final OreVariant NETHERRACK;
    public static final OreVariant BLACKSTONE;
    public static final OreVariant BASALT;

    public static final OreVariant END_STONE;

    private static OreVariant register(String name, OreVariant variant) {
        return Registry.register(OreVariant.REGISTRY, new Identifier(GTValues.MODID, name), variant);
    }

    static {
        STONE = register("stone", new OreVariant(new OreVariant.Settings()
            .blockNameTemplate("{material}_ore")
            .generationRule(OreFeatureConfig.Rules.STONE_ORE_REPLACEABLES)
            .strength(3.0f, 3.0f)
            .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/stone"))
        ));

        DEEPSLATE = register("deepslate", new OreVariant(new OreVariant.Settings()
            .blockNameTemplate("deepslate_{material}_ore")
            .generationRule(OreFeatureConfig.Rules.DEEPSLATE_ORE_REPLACEABLES)
            .strength(4.5f, 3.0f)
            .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/deepslate"))
            .soundGroup(BlockSoundGroup.DEEPSLATE)
        ));

        BEDROCK = register("bedrock", new OreVariant(new OreVariant.Settings()
           .blockNameTemplate("bedrock_{material}_ore")
           .generationRule(new BlockMatchRuleTest(Blocks.BEDROCK))
           .unbreakable()
           .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/bedrock"))
        ));

        GRAVEL = register("gravel", new OreVariant(new OreVariant.Settings()
            .blockNameTemplate("gravel_{material}_ore")
            .generationRule(new BlockMatchRuleTest(Blocks.GRAVEL))
            .strength(1.4f, 1.4f)
            .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/gravel"))
            .soundGroup(BlockSoundGroup.GRAVEL)
            .mineableTag(BlockTags.SHOVEL_MINEABLE)
            .affectedByGravity(0xFF807C7B)
        ));

        SANDSTONE = register("sandstone", new OreVariant(new OreVariant.Settings()
            .blockNameTemplate("sandstone_{material}_ore")
            .generationRule(new BlockMatchRuleTest(Blocks.SANDSTONE))
            .strength(1.8f, 1.8f)
            .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/sandstone"))
        ));

        NETHERRACK = register("netherrack", new OreVariant(new OreVariant.Settings()
            .blockNameTemplate("nether_{material}_ore")
            .generationRule(OreFeatureConfig.Rules.NETHERRACK)
            .strength(1.4f, 1.4f)
            .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/netherrack"))
            .soundGroup(BlockSoundGroup.NETHERRACK)
        ));

        BLACKSTONE = register("blackstone", new OreVariant(new OreVariant.Settings()
            .blockNameTemplate("blackstone_{material}_ore")
            .generationRule(new BlockMatchRuleTest(Blocks.BLACKSTONE))
            .strength(4.0f, 3.0f)
            .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/blackstone"))
        ));

        BASALT = register("basalt", new OreVariant(new OreVariant.Settings()
            .blockNameTemplate("basalt_{material}_ore")
            .generationRule(new BlockMatchRuleTest(Blocks.BASALT))
            .strength(2.25f, 4.2f)
            .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/basalt"))
            .soundGroup(BlockSoundGroup.BASALT)
        ));

        END_STONE = register("end_stone", new OreVariant(new OreVariant.Settings()
            .blockNameTemplate("end_{material}_ore")
            .generationRule(new BlockMatchRuleTest(Blocks.END_STONE))
            .strength(4.0f, 9.0f)
            .modelPath(new Identifier(GTValues.MODID, "block/ore_variant/end_stone"))
        ));
    }
}
