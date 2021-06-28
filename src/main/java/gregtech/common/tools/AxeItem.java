package gregtech.common.tools;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.item.CustomDamageItem;
import gregtech.api.items.toolitem.ToolItemSettings;
import gregtech.api.items.toolitem.ToolItemType;
import gregtech.api.unification.material.type.Material;
import gregtech.mixin.accessor.AxeItemAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Map;
import java.util.Optional;

public class AxeItem extends MiningToolItem {

    public AxeItem(ToolItemSettings settings, ToolItemType toolItemType, Material material) {
        super(settings, toolItemType, material);
    }

    private static Optional<BlockState> tryConvertBlockAndPlayEffects(ItemUsageContext context) {
        World world = context.getWorld();

        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        Map<Block, Block> unwaxedBlockMap = HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get();

        Optional<BlockState> strippedState = ((AxeItemAccessor) Items.IRON_AXE).getStrippedState(blockState);

        if (strippedState.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return strippedState;
        }

        Optional<BlockState> deoxidizedState = Oxidizable.getDecreasedOxidationState(blockState);

        if (deoxidizedState.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, WorldEvents.BLOCK_SCRAPED, blockPos, 0);

            return deoxidizedState;
        }

        Optional<BlockState> unwaxedState = Optional.ofNullable(unwaxedBlockMap.get(blockState.getBlock()))
                .map(block -> block.getStateWithProperties(blockState));

        if (unwaxedState.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, WorldEvents.WAX_REMOVED, blockPos, 0);

            return unwaxedState;
        }
        return Optional.empty();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();

        if (playerEntity == null) {
            return ActionResult.PASS;
        }

        if (!canDamageItem(itemStack, damagePerSpecialAction, playerEntity)) {
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
