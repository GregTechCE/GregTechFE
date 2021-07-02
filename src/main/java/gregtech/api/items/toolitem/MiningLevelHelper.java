package gregtech.api.items.toolitem;

import com.google.common.collect.ImmutableList;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

public class MiningLevelHelper {

    //See MiningToolItem#isSuitableFor(BlockState)
    private static final List<Pair<Integer, Tag.Identified<Block>>> MINING_LEVELS = ImmutableList.of(
            Pair.of(MiningLevels.DIAMOND, BlockTags.NEEDS_DIAMOND_TOOL),
            Pair.of(MiningLevels.IRON, BlockTags.NEEDS_IRON_TOOL),
            Pair.of(MiningLevels.STONE, BlockTags.NEEDS_STONE_TOOL)
    );

    public static void addTagForHarvestLevel(int harvestLevel, Set<Tag.Identified<Block>> outTags) {
        //Check each mining level, if harvest level matches, append the tag
        for (Pair<Integer, Tag.Identified<Block>> pair : MINING_LEVELS) {
            if (pair.getLeft().equals(harvestLevel)) {
                outTags.add(pair.getRight());
                return;
            }
        }

        //Only remaining vanilla material is wood, which doesn't have associated tag
    }

    public static boolean checkHarvestLevelRequirements(BlockState blockState, int toolHarvestLevel) {
        //We can always harvest blocks without tool requirements
        if (!blockState.isToolRequired()) {
            return true;
        }

        //Check each mining level, if block is tagged as one, we can harvest it if our tool level
        //is equal to or higher than associated tag's mining level
        for (Pair<Integer, Tag.Identified<Block>> pair : MINING_LEVELS) {
            if (blockState.isIn(pair.getRight())) {
                return toolHarvestLevel >= pair.getLeft();
            }
        }

        //Only remaining vanilla material is wood, which doesn't have associated tag
        return toolHarvestLevel >= MiningLevels.WOOD;
    }
}
