package gregtech.api.multiblock.util;

import gregtech.api.multiblock.BlockWorldState;

import java.util.function.Predicate;

public class BlockCountPredicate {

    private final Predicate<BlockWorldState> predicate;
    private final int minMatches;
    private final int maxMatches;

    public BlockCountPredicate(Predicate<BlockWorldState> predicate, int minMatches, int maxMatches) {
        this.predicate = predicate;
        this.minMatches = minMatches;
        this.maxMatches = maxMatches;
    }

    public boolean test(BlockWorldState worldState) {
        return this.predicate.test(worldState);
    }

    public boolean testCount(int count) {
        return count >= this.minMatches && count <= this.maxMatches;
    }

    public int getMinMatches() {
        return minMatches;
    }

    public int getMaxMatches() {
        return maxMatches;
    }
}
