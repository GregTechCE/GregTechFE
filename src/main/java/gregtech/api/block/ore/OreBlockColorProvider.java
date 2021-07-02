package gregtech.api.block.ore;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.MaterialFlags;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public enum OreBlockColorProvider implements BlockColorProvider, ItemColorProvider {
    INSTANCE;

    @Override
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        if (tintIndex == 1 && state.getBlock() instanceof OreBlock oreBlock) {
            Material material = oreBlock.getMaterial();
            return material.queryPropertyChecked(MaterialFlags.COLOR);
        }
        return 0xFFFFFF;
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            return getColor(blockItem.getBlock().getDefaultState(), null, null, tintIndex);
        }
        return 0xFFFFFF;
    }
}
