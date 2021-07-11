package gregtech.api.block.util;

import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

public interface ModelStateProviderBlock {

    default Identifier getStateModelLocation() {
        Identifier blockId = Registry.BLOCK.getId((Block) this);
        return new Identifier(blockId.getNamespace(), "block/" + blockId.getPath());
    }

    ModelStateManager<Block> getModelStateManager();

    ModelState<Block> getModelState(BlockRenderView blockView, BlockState state, BlockPos pos);

    ModelState<Block> getModelStateForItem(ItemStack itemStack);
}
