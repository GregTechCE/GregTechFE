package gregtech.api.multiblock.impl;

import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.multiblock.util.BlockCountMatcher;
import gregtech.api.multiblock.util.StructureCenterOffset;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BlockPatternCheckerImpl {

    public static PatternMatchContext checkPatternAt(BlockPattern pattern, World world, BlockPos centerPos, Direction facing) {
        BlockCountMatcher countMatcher = pattern.createBlockCountMatcher();
        MutablePatternMatchContext matchContext = new MutablePatternMatchContext();
        MutablePatternMatchContext layerContext = new MutablePatternMatchContext();

        StructureCenterOffset centerOffset = pattern.getCenterOffset();
        MutableBlockWorldState worldState = new MutableBlockWorldState(world, matchContext, layerContext);
        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        boolean findFirstAisle = false;
        int minZ = -centerOffset.getMaxZ();

        //Checking aisles
        for (int fingerIndex = 0, z = minZ++, repetitionIndex; fingerIndex < pattern.getFingerLength(); fingerIndex++) {
            //Checking repeatable slices
            loop: for (repetitionIndex = 0; (findFirstAisle ? repetitionIndex < pattern.getAisleRepetitions(fingerIndex).getMaxRepetitions() : z <= -centerOffset.getMinZ()); repetitionIndex++) {
                //Checking single slice
                layerContext.resetContext();

                for (int thumbIndex = 0, y = -centerOffset.getPos().getY(); thumbIndex < pattern.getThumbLength(); thumbIndex++, y++) {
                    for (int palmIndex = 0, x = -centerOffset.getPos().getX(); palmIndex < pattern.getPalmLength(); palmIndex++, x++) {

                        pattern.setActualRelativeOffset(blockPos, x, y, z, facing);
                        blockPos.set(blockPos.getX() + centerPos.getX(), blockPos.getY() + centerPos.getY(), blockPos.getZ() + centerPos.getZ());
                        worldState.update(blockPos);

                        if (!pattern.checkBlockAt(worldState, palmIndex, thumbIndex, fingerIndex)) {
                            if (findFirstAisle) {
                                //retreat to see if the first aisle can start later
                                if (!pattern.getAisleRepetitions(fingerIndex).check(repetitionIndex)) {
                                    fingerIndex = 0;
                                    repetitionIndex = 0;
                                    z = minZ++;
                                    matchContext.resetContext();
                                    findFirstAisle = false;
                                }
                            } else {
                                //continue searching for the first aisle
                                z++;
                            }
                            continue loop;
                        }
                        countMatcher.trackMatchedBlock(worldState);
                    }
                }
                findFirstAisle = true;
                z++;

                //Check layer-local matcher predicate
                if (!pattern.checkStructureLayer(layerContext, fingerIndex)) {
                    return null;
                }
            }

            //Repetitions out of range
            if (!pattern.getAisleRepetitions(fingerIndex).check(repetitionIndex)) {
                return null;
            }
        }

        //Check count matches amount
        if (!countMatcher.matches()) {
            return null;
        }

        //Check general match predicates
        if (!pattern.checkStructure(matchContext)) {
            return null;
        }
        return matchContext;
    }
}
