package gregtech.api.multiblock;

import java.util.function.Predicate;

public interface PatternCenterPredicate extends Predicate<BlockWorldState> {

    static PatternCenterPredicate from(Predicate<BlockWorldState> predicate) {
        return predicate::test;
    }
}
