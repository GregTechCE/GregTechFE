package gregtech.api.capability.internal;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Block Entities implementing this interface will be considered a part of multiblock structures if they allow it
 * and can affect their functionality in various ways by implementing ability types
 */
public interface MultiblockPart {

    boolean isAttachedToMultiBlock();

    void addToMultiBlock(World world, BlockPos controllerPos);

    void removeFromMultiBlock(World world, BlockPos controllerPos);

    void addAbilities(MultiblockAbilityList abilityList);
}
