package gregtech.api.util;

import com.google.common.base.Preconditions;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.world.GTGameRules;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class OvervoltageHelper {

    public static void doOvervoltageExplosion(MachineBlockEntity machine, VoltageTier tier) {
        doOvervoltageExplosion(Preconditions.checkNotNull(machine.getWorld()), machine.getPos(), tier);
    }

    public static void doOvervoltageExplosion(World world, BlockPos blockPos, VoltageTier tier) {
        boolean allowOvervoltageExplosions = world.getGameRules().getBoolean(GTGameRules.DO_OVERVOLTAGE_EXPLOSIONS);

        float explosionStrength = 1.0f + tier.ordinal() * 0.5f;
        Explosion.DestructionType destructionType = Explosion.DestructionType.BREAK;

        if (!allowOvervoltageExplosions) {
            destructionType = Explosion.DestructionType.NONE;
        }

        world.createExplosion(null, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, explosionStrength, false, destructionType);
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
    }
}
