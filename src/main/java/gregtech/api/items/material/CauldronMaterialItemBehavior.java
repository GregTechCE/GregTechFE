package gregtech.api.items.material;

import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum CauldronMaterialItemBehavior implements CauldronBehavior {
    INSTANCE;

    @Override
    public ActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        if (stack.getItem() instanceof MaterialItem materialItem) {
            TypedActionResult<ItemStack> result = materialItem.tryPurifyItem(stack, world, pos);

            if (result.getResult().isAccepted()) {
                player.setStackInHand(hand, result.getValue());
                return ActionResult.SUCCESS;
            }
            return result.getResult();
        }
        return ActionResult.PASS;
    }
}
