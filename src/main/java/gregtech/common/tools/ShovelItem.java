package gregtech.common.tools;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.item.CustomDamageItem;
import gregtech.api.items.toolitem.ToolItemSettings;
import gregtech.api.items.toolitem.ToolItemType;
import gregtech.api.unification.material.Material;
import gregtech.mixin.accessor.ShovelItemAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Map;
import java.util.Optional;

public class ShovelItem extends MiningToolItem {

    public ShovelItem(ToolItemSettings settings, ToolItemType toolItemType, Material material) {
        super(settings, toolItemType, material);
    }

    private static Optional<BlockState> tryConvertBlockAndPlayEffects(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        PlayerEntity playerEntity = context.getPlayer();

        if (context.getSide() == Direction.DOWN) {
            return Optional.empty();
        }

        Map<Block, BlockState> pathStatesMap = ShovelItemAccessor.getPathStates();
        BlockState pathBlockState = pathStatesMap.get(blockState.getBlock());

        if (pathBlockState != null && world.getBlockState(blockPos.up()).isAir()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return Optional.of(pathBlockState);
        }

        if (blockState.getBlock() instanceof CampfireBlock && blockState.get(CampfireBlock.LIT)) {
            if (!world.isClient()) {
                world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, blockPos, 0);
            }

            CampfireBlock.extinguish(context.getPlayer(), world, blockPos, blockState);
            return Optional.of(blockState.with(CampfireBlock.LIT, false));
        }
        return Optional.empty();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();

        if (playerEntity == null) {
            return ActionResult.PASS;
        }

        if (!canDamageItem(context.getStack(), damagePerSpecialAction, playerEntity)) {
            return ActionResult.PASS;
        }

        Optional<BlockState> convertedState = tryConvertBlockAndPlayEffects(context);

        if (convertedState.isPresent()) {
            if (!world.isClient) {
                world.setBlockState(blockPos, convertedState.get(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                CustomDamageItem.damageItem(playerEntity, context.getHand(), damagePerSpecialAction, Simulation.ACTION);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
