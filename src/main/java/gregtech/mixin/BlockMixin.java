package gregtech.mixin;

import gregtech.api.items.util.DropConversionTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true)
    private void tweakDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, Entity entity, ItemStack itemStack, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
        if (itemStack.getItem() instanceof DropConversionTool dropConversionTool) {
            List<ItemStack> drops = new ArrayList<>(callbackInfo.getReturnValue());
            dropConversionTool.convertBlockDrops(itemStack, state, world, pos, blockEntity, entity, drops);
            callbackInfo.setReturnValue(drops);
        }
    }
}
