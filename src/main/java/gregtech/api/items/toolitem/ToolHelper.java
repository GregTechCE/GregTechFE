package gregtech.api.items.toolitem;

import com.google.common.collect.ImmutableList;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ToolHelper {

    //See MiningToolItem#isSuitableFor(BlockState)
    private static final List<Pair<Integer, Tag<Block>>> MINING_LEVELS = ImmutableList.of(
            Pair.of(MiningLevels.DIAMOND, BlockTags.NEEDS_DIAMOND_TOOL),
            Pair.of(MiningLevels.IRON, BlockTags.NEEDS_IRON_TOOL),
            Pair.of(MiningLevels.STONE, BlockTags.NEEDS_STONE_TOOL)
    );

    public static boolean checkHarvestLevelRequirements(BlockState blockState, int toolHarvestLevel) {
        //We can always harvest blocks without tool requirements
        if (!blockState.isToolRequired()) {
            return true;
        }

        //Check each mining level, if block is tagged as one, we can harvest it if our tool level
        //is equal to or higher than associated tag's mining level
        for (Pair<Integer, Tag<Block>> pair : MINING_LEVELS) {
            if (blockState.isIn(pair.getRight())) {
                return toolHarvestLevel >= pair.getLeft();
            }
        }

        //Only remaining vanilla material is wood, which doesn't have associated tag
        return toolHarvestLevel >= MiningLevels.WOOD;
    }
}
