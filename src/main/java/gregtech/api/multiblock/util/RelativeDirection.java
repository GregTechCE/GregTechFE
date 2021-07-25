package gregtech.api.multiblock.util;

import net.minecraft.util.math.Direction;

import java.util.function.Function;

/**
 * Relative direction when facing horizontally
 */
public enum RelativeDirection {
    UP(f -> Direction.UP),
    DOWN(f -> Direction.DOWN),
    LEFT(Direction::rotateYCounterclockwise),
    RIGHT(Direction::rotateYClockwise),
    FRONT(Function.identity()),
    BACK(Direction::getOpposite);

    private final Function<Direction, Direction> actualFacing;

    RelativeDirection(Function<Direction, Direction> actualFacing) {
        this.actualFacing = actualFacing;
    }

    public Direction getActualFacing(Direction facing) {
        return actualFacing.apply(facing);
    }
}
