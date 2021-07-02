package gregtech.api.unification.ore;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class OreVariant {

    public static final Registry<OreVariant> REGISTRY = FabricRegistryBuilder
            .createSimple(OreVariant.class, new Identifier(GTValues.MODID, "ore_variants"))
            .buildAndRegister();

    private final String blockNameTemplate;
    private final Tag.Identified<Block> mineableTag;
    private final Identifier modelPath;
    private final RuleTest generationRule;
    private final float hardness;
    private final float blastResistance;
    private final int baseHarvestLevel;
    private final BlockSoundGroup soundGroup;
    private final boolean affectedByGravity;

    public OreVariant(Settings settings) {
        Preconditions.checkNotNull(settings.modelPath, "modelPath not set");
        Preconditions.checkNotNull(settings.generationRule, "generationRule not set");
        Preconditions.checkNotNull(settings.blockNameTemplate, "blockNameTemplate not set");

        this.blockNameTemplate = settings.blockNameTemplate;
        this.mineableTag = settings.mineableTag;
        this.modelPath = settings.modelPath;
        this.generationRule = settings.generationRule;
        this.hardness = settings.hardness;
        this.blastResistance = settings.blastResistance;
        this.baseHarvestLevel = settings.baseHarvestLevel;
        this.soundGroup = settings.soundGroup;
        this.affectedByGravity = settings.affectedByGravity;
    }

    public Identifier getName() {
        return Preconditions.checkNotNull(REGISTRY.getId(this), "OreVariant not registered");
    }

    public String createBlockName(String materialName) {
        return this.blockNameTemplate.replace("{material}", materialName);
    }

    public Tag.Identified<Block> getMineableTag() {
        return mineableTag;
    }

    public Identifier getModelPath() {
        return modelPath;
    }

    public RuleTest getGenerationRule() {
        return generationRule;
    }

    public float getHardness() {
        return hardness;
    }

    public float getBlastResistance() {
        return blastResistance;
    }

    public int getBaseHarvestLevel() {
        return baseHarvestLevel;
    }

    public BlockSoundGroup getSoundGroup() {
        return soundGroup;
    }

    public boolean isAffectedByGravity() {
        return affectedByGravity;
    }

    @Override
    public String toString() {
        return String.valueOf(REGISTRY.getId(this));
    }

    public static class Settings {

        private String blockNameTemplate;
        private Tag.Identified<Block> mineableTag = BlockTags.PICKAXE_MINEABLE;
        private RuleTest generationRule;
        private float hardness;
        private float blastResistance;
        private int baseHarvestLevel = MiningLevels.STONE;
        private BlockSoundGroup soundGroup = BlockSoundGroup.STONE;
        private boolean affectedByGravity = false;
        private Identifier modelPath;

        public Settings blockNameTemplate(String blockNameTemplate) {
            Preconditions.checkNotNull(blockNameTemplate);
            this.blockNameTemplate = blockNameTemplate;
            return this;
        }

        public Settings generationRule(RuleTest generationRule) {
            Preconditions.checkNotNull(generationRule);
            this.generationRule = generationRule;
            return this;
        }

        public Settings strength(float hardness, float blastResistance) {
            this.hardness = hardness;
            this.blastResistance = blastResistance;
            return this;
        }

        public Settings modelPath(Identifier modelPath) {
            Preconditions.checkNotNull(modelPath);
            this.modelPath = modelPath;
            return this;
        }

        public Settings soundGroup(BlockSoundGroup soundGroup) {
            Preconditions.checkNotNull(soundGroup);
            this.soundGroup = soundGroup;
            return this;
        }

        public Settings mineableTag(Tag.Identified<Block> mineableTag) {
            Preconditions.checkNotNull(mineableTag);
            this.mineableTag = mineableTag;
            return this;
        }

        public Settings miningLevel(int baseHarvestLevel) {
            this.baseHarvestLevel = baseHarvestLevel;
            return this;
        }

        public Settings unbreakable() {
            this.hardness = -1.0f;
            return this;
        }

        public Settings affectedByGravity() {
            this.affectedByGravity = true;
            return this;
        }
    }
}
