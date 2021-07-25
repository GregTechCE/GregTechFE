package gregtech.api.multiblock.util;

import net.minecraft.util.math.BlockPos;

public class StructureCenterOffset {

    private final BlockPos pos;
    private final int minZ;
    private final int maxZ;

    public StructureCenterOffset(BlockPos pos, int minZ, int maxZ) {
        this.pos = pos;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public BlockPos getPos() {
        return pos;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }
}
