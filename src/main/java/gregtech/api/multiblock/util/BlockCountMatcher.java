package gregtech.api.multiblock.util;

import com.google.common.collect.ImmutableList;
import gregtech.api.multiblock.BlockWorldState;

public class BlockCountMatcher {

    private final ImmutableList<BlockCountPredicate> predicates;
    private final int[] countMatches;

    public BlockCountMatcher(ImmutableList<BlockCountPredicate> predicates) {
        this.predicates = predicates;
        this.countMatches = new int[predicates.size()];
    }

    public void trackMatchedBlock(BlockWorldState worldState) {
        for (int i = 0; i < this.countMatches.length; i++) {
            if (this.predicates.get(i).test(worldState)) {
                this.countMatches[i]++;
            }
        }
    }

    public boolean matches() {
        for (int i = 0; i < this.countMatches.length; i++) {
            int count = this.countMatches[i];
            if (!this.predicates.get(i).testCount(count)) {
                return false;
            }
        }
        return true;
    }
}
