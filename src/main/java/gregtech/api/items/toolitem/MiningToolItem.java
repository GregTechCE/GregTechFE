package gregtech.api.items.toolitem;

import gregtech.api.unification.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.tag.Tag;

public class MiningToolItem extends ToolItem {
    Tag<Block> effectiveBlocks;

    public MiningToolItem(ToolItemSettings settings, ToolItemType toolItemType, Material material,
                          Tag<Block> effectiveBlocks) {
        super(settings, toolItemType, material);
        this.effectiveBlocks = effectiveBlocks;
    }

    @Override
    public boolean canApplyEnchantment(Enchantment enchantment) {
        return enchantment.type == EnchantmentTarget.DIGGER ||
                enchantment.type.isAcceptableItem(this);
    }

    @Override
    protected boolean isCorrectToolForBlock(BlockState state) {
        return state.isIn(this.effectiveBlocks);
    }
}
