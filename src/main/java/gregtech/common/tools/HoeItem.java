package gregtech.common.tools;

import alexiil.mc.lib.attributes.Simulation;
import com.mojang.datafixers.util.Pair;
import gregtech.api.capability.item.CustomDamageItem;
import gregtech.api.items.toolitem.ToolItemSettings;
import gregtech.api.items.toolitem.ToolItemType;
import gregtech.api.unification.material.type.Material;
import gregtech.mixin.accessor.HoeItemAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class HoeItem extends MiningToolItem {

    public HoeItem(ToolItemSettings settings, ToolItemType toolItemType, Material material) {
        super(settings, toolItemType, material);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        PlayerEntity playerEntity = context.getPlayer();

        if (!canDamageItem(context.getStack(), this.damagePerSpecialAction, playerEntity)) {
            return ActionResult.PASS;
        }

        if (playerEntity == null) {
            return ActionResult.PASS;
        }

        Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair =
                ((HoeItemAccessor) Items.IRON_HOE).getTilledBlocks().get(blockState.getBlock());
        if (pair == null) {
            return ActionResult.PASS;
        }

        if (pair.getFirst().test(context)) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (!world.isClient) {
                pair.getSecond().accept(context);
                CustomDamageItem.damageItem(playerEntity, context.getHand(), damagePerSpecialAction, Simulation.ACTION);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
