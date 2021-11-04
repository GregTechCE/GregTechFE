package gregtech.common.tools;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.item.CustomDamageItem;
import gregtech.api.items.toolitem.ToolItem;
import gregtech.api.unification.material.Material;
import gregtech.api.util.function.Task;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class TreeChopTask implements Task {

    private static final int MAX_BLOCKS_SEARCH_PER_TICK = 1024;
    private static final int MAX_BLOCKS_TO_SEARCH = 8192;
    private final Stack<Pair<MultiFacing, Boolean>> moveStack = new Stack<>();
    private boolean isLastBlockLeaves = false;
    private final BlockPos.Mutable currentPos = new BlockPos.Mutable();
    private final BlockPos startBlockPos;
    private final Set<BlockPos> visitedBlockPos = new HashSet<>();
    private final List<BlockPos> woodBlockPos = new ArrayList<>();

    private boolean finishedSearchingBlocks = false;
    private int currentWoodBlockIndex = 0;
    private final World world;
    private final ServerPlayerEntity player;
    private final ItemStack itemStack;

    public TreeChopTask(BlockPos startPos, World world, ServerPlayerEntity player, ItemStack toolStack) {
        this.startBlockPos = startPos.toImmutable();
        this.currentPos.set(startPos);
        this.woodBlockPos.add(startPos.toImmutable());
        this.visitedBlockPos.add(startPos.toImmutable());
        this.world = world;
        this.itemStack = toolStack.copy();
        this.player = player;
        player.getItemCooldownManager().set(itemStack.getItem(), 20);
    }

    @Override
    public boolean run() {
        boolean isPlayerDisconnected = player.isDisconnected();
        boolean isPlayerNear = player.world == world && currentPos.getSquaredDistance(player.getPos().x, currentPos.getY(), player.getPos().z, true) <= 1024;
        ItemStack itemInMainHand = this.player.getMainHandStack();
        if (isPlayerDisconnected || !isPlayerNear || itemInMainHand.isEmpty() || !isItemEqual(itemInMainHand)) {
            return false;
        }
        if (world.getTime() % 10 == 0)
            player.getItemCooldownManager().set(itemStack.getItem(), 20);

        if(!finishedSearchingBlocks) {
            this.finishedSearchingBlocks = !attemptSearchWoodBlocks() ||
                    this.visitedBlockPos.size() >= MAX_BLOCKS_TO_SEARCH;
            if(finishedSearchingBlocks) {
                this.woodBlockPos.sort(new WoodBlockComparator());
            }
            return true;
        }
        return tryBreakAny((ToolItem) itemStack.getItem());
    }

    private class WoodBlockComparator implements Comparator<BlockPos> {

        @Override
        public int compare(BlockPos o1, BlockPos o2) {
            int a = -Integer.compare(o1.getY(), o2.getY());
            if(a != 0) {
                return a;
            }
            return Integer.compare(distance(o1), distance(o2));
        }

        private int distance(BlockPos pos) {
            int diffX = pos.getX() - startBlockPos.getX();
            int diffZ = pos.getZ() - startBlockPos.getZ();
            return diffX * diffX + diffZ * diffZ;
        }
    }

    private boolean isItemEqual(ItemStack heldItem) {
        if (heldItem.getItem() != itemStack.getItem() || !(heldItem.getItem() instanceof ToolItem)) {
            return false;
        }
        Material heldToolMaterial = ((ToolItem) heldItem.getItem()).getMaterial();
        Material toolMaterial = ((ToolItem) itemStack.getItem()).getMaterial();
        return toolMaterial == heldToolMaterial;
    }

    private boolean tryBreakAny(ToolItem toolItem) {
        if(woodBlockPos.size() > currentWoodBlockIndex) {
            BlockPos woodPos = woodBlockPos.get(currentWoodBlockIndex++);
            BlockState blockState = this.world.getBlockState(woodPos);
            if(isLogBlock(blockState) == 1) {
                if (CustomDamageItem.damageItem(this.player, Hand.MAIN_HAND, toolItem.getDamagePerBlockBreak(), Simulation.ACTION)){
                    return this.world.breakBlock(woodPos, true);
                }
            }
        }
        return false;
    }

    private boolean attemptSearchWoodBlocks() {
        int blocksSearchedNow = 0;
        int validWoodBlocksFound = 0;
        main: while (blocksSearchedNow <= MAX_BLOCKS_SEARCH_PER_TICK) {
            //try to iterate neighbour blocks
            blocksSearchedNow++;
            for (MultiFacing facing : MultiFacing.VALUES) {
                //move at facing
                facing.move(currentPos);

                if(!visitedBlockPos.contains(currentPos)) {
                    BlockState blockState = this.world.getBlockState(currentPos);
                    int blockType = isLogBlock(blockState);
                    boolean currentIsLeavesBlock = isLastBlockLeaves;
                    if (blockType == 1 || (blockType == 2 && !isLastBlockLeaves)) {
                        this.isLastBlockLeaves = blockType == 2;
                        BlockPos immutablePos = currentPos.toImmutable();
                        this.visitedBlockPos.add(immutablePos);
                        if(blockType == 1) {
                            this.woodBlockPos.add(immutablePos);
                        }
                        validWoodBlocksFound++;
                        moveStack.add(Pair.of(facing.getOpposite(), currentIsLeavesBlock));
                        continue main;
                    }
                }

                //move back if it wasn't a tree block
                facing.getOpposite().move(currentPos);
            }
            //we didn't found any matching block in neighbours - move back
            if (!moveStack.isEmpty()) {
                Pair<MultiFacing, Boolean> prevData = moveStack.pop();
                prevData.getLeft().move(currentPos);
                this.isLastBlockLeaves = prevData.getRight();
            } else break;
        }
        return validWoodBlocksFound > 0;
    }

    public static int isLogBlock(BlockState blockState) {
        if(blockState.isIn(BlockTags.LOGS)) {
            return 1;
        } else if(blockState.isIn(BlockTags.LEAVES)) {
            return 2;
        }
        return 0;
    }

    private enum MultiFacing {
        UP(0, new Vec3i(0, 1, 0)),
        DOWN(1, new Vec3i(0, -1, 0)),
        SOUTH(2, new Vec3i(0, 0, 1)),
        NORTH(3, new Vec3i(0, 0, -1)),
        EAST(4, new Vec3i(1, 0, 0)),
        WEST(5, new Vec3i(-1, 0, 0)),

        SOUTH_DOWN(10, new Vec3i(0, -1, 1)),
        NORTH_DOWN(11, new Vec3i(0, -1, -1)),
        EAST_DOWN(12, new Vec3i(1, -1, 0)),
        WEST_DOWN(13, new Vec3i(-1, -1, 0)),

        SOUTH_UP(6, new Vec3i(0, 1, 1)),
        NORTH_UP(7, new Vec3i(0, 1, -1)),
        EAST_UP(8, new Vec3i(1, 1, 0)),
        WEST_UP(9, new Vec3i(-1, 1, 0)),

        SOUTH_EAST_DOWN(18, new Vec3i(1, -1, 1)),
        SOUTH_WEST_DOWN(19, new Vec3i(-1, -1, 1)),
        NORTH_EAST_DOWN(20, new Vec3i(-1, -1, 1)),
        NORTH_WEST_DOWN(21, new Vec3i(-1, -1, -1)),

        SOUTH_EAST_UP(14, new Vec3i(1, 1, 1)),
        SOUTH_WEST_UP(15, new Vec3i(1, 1, -1)),
        NORTH_EAST_UP(16, new Vec3i(1, 1, -1)),
        NORTH_WEST_UP(17, new Vec3i(-1, 1, -1));


        private final int oppositeIndex;
        private final Vec3i direction;
        private static final MultiFacing[] VALUES = values();

        MultiFacing(int oppositeIndex, Vec3i direction) {
            this.oppositeIndex = oppositeIndex;
            this.direction = direction;
        }

        public void move(BlockPos.Mutable blockPos) {
            blockPos.set(blockPos.getX() + direction.getX(),
                    blockPos.getY() + direction.getY(),
                    blockPos.getZ() + direction.getZ());
        }

        public MultiFacing getOpposite() {
            return VALUES[oppositeIndex];
        }
    }

}