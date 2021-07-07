package gregtech.api.block.ore;

import gregtech.api.block.util.AutoTaggedBlock;
import gregtech.api.block.util.LootTableAwareBlock;
import gregtech.api.items.material.MaterialItemForms;
import gregtech.api.items.material.MaterialItemId;
import gregtech.api.items.material.MaterialItemRegistry;
import gregtech.api.items.toolitem.MiningLevelHelper;
import gregtech.api.unification.forms.MaterialForms;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.ore.OreVariant;
import gregtech.mixin.accessor.BlockLootTableGeneratorAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;
import java.util.Set;

public class OreBlock extends FallingBlock implements AutoTaggedBlock, LootTableAwareBlock {

    private final OreVariant variant;
    private final Material material;

    public OreBlock(Settings settings, OreVariant variant, Material material) {
        super(settings);
        this.variant = variant;
        this.material = material;
        this.lootTableId = LootTables.EMPTY;
    }

    public OreVariant getVariant() {
        return variant;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public MutableText getName() {
        return this.variant.getDisplayName(this.material);
    }

    @Override
    public void addBlockTags(Set<Tag.Identified<Block>> outTags) {
        outTags.add(MaterialForms.ORES.getBlockTag(this.material));

        int materialHarvestLevel = this.material.queryPropertyChecked(MaterialFlags.HARVEST_LEVEL);
        int baseHarvestLevel = this.variant.getBaseHarvestLevel();

        outTags.add(this.variant.getMineableTag());
        MiningLevelHelper.addTagForHarvestLevel(Math.max(baseHarvestLevel, materialHarvestLevel), outTags);
    }

    @Override
    public LootTable.Builder generateLootTable() {
        MaterialItemId itemId = new MaterialItemId(MaterialItemForms.RAW_RESOURCE, material);
        Item rawResourceItem = MaterialItemRegistry.INSTANCE.getMaterialItem(itemId);

        return BlockLootTableGeneratorAccessor.oreDrops(this, rawResourceItem);
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return this.variant.getFallingParticleColor();
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (this.variant.isAffectedByGravity()) {
            world.getBlockTickScheduler().schedule(pos, this, this.getFallDelay());
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (this.variant.isAffectedByGravity()) {
            world.getBlockTickScheduler().schedule(pos, this, this.getFallDelay());
        }
        return state;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (this.variant.isAffectedByGravity()) {
            super.scheduledTick(state, world, pos, random);
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (this.variant.isAffectedByGravity()) {
            super.randomDisplayTick(state, world, pos, random);
        }
    }
}
