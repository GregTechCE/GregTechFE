package gregtech.api.item.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface DropConversionTool {

    /**
     * Called when block drops are retrieved using {@link net.minecraft.block.Block#getDroppedStacks}
     * Keep in mind it will only be called when item context is available, and some mods might not respect it
     * and call BlockState.getDrops directly, thus bypassing the tool logic
     *
     * @param world world the block is broken in
     * @param stack tool used to break the block. Usually item in player's main hand
     * @param state state of the block being broken
     * @param pos position of the block being broken
     * @param blockEntity block entity of the block, may be null
     * @param entity entity breaking the block, usually the player
     * @param drops modifiable list of drops returned from the block
     */
    void convertBlockDrops(ItemStack stack, BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, List<ItemStack> drops);
}
