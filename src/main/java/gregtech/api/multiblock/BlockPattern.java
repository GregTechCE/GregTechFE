package gregtech.api.multiblock;

import com.google.common.collect.ImmutableList;
import gregtech.api.multiblock.impl.BlockPatternCheckerImpl;
import gregtech.api.multiblock.util.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class BlockPattern {

    private final int fingerLength;
    private final int thumbLength;
    private final int palmLength;

    private final RelativeDirection[] structureDir;
    private final Int2ObjectMap<AisleRepetition> aisleRepetitions;
    private final Predicate<BlockWorldState>[][][] blockMatches;

    private final Int2ObjectMap<Predicate<PatternMatchContext>> layerValidators;
    private final List<Predicate<PatternMatchContext>> validators;
    private final ImmutableList<BlockCountPredicate> blockCountValidators;

    private final StructureCenterOffset centerOffset;

    public BlockPattern(RelativeDirection[] structureDir,
                        Int2ObjectMap<AisleRepetition> aisleRepetitions,
                        Predicate<BlockWorldState>[][][] predicatesIn,
                        Int2ObjectMap<Predicate<PatternMatchContext>> layerMatchers,
                        List<Predicate<PatternMatchContext>> validators,
                        Collection<BlockCountPredicate> blockCountValidators) {

        this.fingerLength = predicatesIn.length;
        this.thumbLength = predicatesIn[0].length;
        this.palmLength = predicatesIn[0][0].length;

        this.structureDir = structureDir;
        this.aisleRepetitions = aisleRepetitions;
        this.blockMatches = predicatesIn;

        this.layerValidators = new Int2ObjectOpenHashMap<>(layerMatchers);
        this.validators = ImmutableList.copyOf(validators);
        this.blockCountValidators = ImmutableList.copyOf(blockCountValidators);

        this.centerOffset = initializeCenterOffset();
    }

    private StructureCenterOffset initializeCenterOffset() {
        for (int palmIndex = 0; palmIndex < this.palmLength; palmIndex++) {
            for (int thumbIndex = 0; thumbIndex < this.thumbLength; thumbIndex++) {
                for (int fingerIndex = 0, minZ = 0, maxZ = 0; fingerIndex < this.fingerLength; minZ += getAisleRepetitions(fingerIndex).getMinRepetitions(), maxZ += getAisleRepetitions(fingerIndex).getMaxRepetitions(), fingerIndex++) {
                    Predicate<BlockWorldState> predicate = this.blockMatches[fingerIndex][thumbIndex][palmIndex];
                    if (predicate instanceof PatternCenterPredicate) {
                        return new StructureCenterOffset(new BlockPos(palmIndex, thumbIndex, fingerIndex), minZ, maxZ);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Didn't found center predicate");
    }

    public PatternMatchContext checkPatternAt(World world, BlockPos centerPos, Direction facing) {
        return BlockPatternCheckerImpl.checkPatternAt(this, world, centerPos, facing);
    }

    public int getFingerLength() {
        return this.fingerLength;
    }

    public int getThumbLength() {
        return this.thumbLength;
    }

    public int getPalmLength() {
        return this.palmLength;
    }

    public AisleRepetition getAisleRepetitions(int fingerIndex) {
        return this.aisleRepetitions.getOrDefault(fingerIndex, AisleRepetition.SINGLE);
    }

    public StructureCenterOffset getCenterOffset() {
        return centerOffset;
    }

    public BlockCountMatcher createBlockCountMatcher() {
        return new BlockCountMatcher(this.blockCountValidators);
    }

    public boolean checkStructure(PatternMatchContext matchContext) {
        for (Predicate<PatternMatchContext> contextPredicate : this.validators) {
            if (!contextPredicate.test(matchContext)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkStructureLayer(PatternMatchContext layerContext, int fingerIndex) {
        Predicate<PatternMatchContext> layerPredicate = this.layerValidators.get(fingerIndex);
        return layerPredicate == null || layerPredicate.test(layerContext);
    }

    public boolean checkBlockAt(BlockWorldState state, int palmIndex, int thumbIndex, int fingerIndex) {
        return this.blockMatches[fingerIndex][thumbIndex][palmIndex].test(state);
    }

    public void setActualRelativeOffset(BlockPos.Mutable pos, int x, int y, int z, Direction facing) {
        int[] c0 = new int[] {x, y, z};
        int[] c1 = new int[3];
        for (int i = 0; i < 3; i++) {
            switch (structureDir[i].getActualFacing(facing)) {
                case UP -> c1[1] = c0[i];
                case DOWN -> c1[1] = -c0[i];
                case WEST -> c1[0] = -c0[i];
                case EAST -> c1[0] = c0[i];
                case NORTH -> c1[2] = -c0[i];
                case SOUTH -> c1[2] = c0[i];
            }
        }
        pos.set(c1[0], c1[1], c1[2]);
    }
}
