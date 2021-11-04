package gregtech.common.tools;

import gregtech.api.items.toolitem.ToolItem;
import gregtech.api.items.toolitem.ToolItemSettings;
import gregtech.api.items.toolitem.ToolItemType;
import gregtech.api.unification.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;

public class SwordItem extends ToolItem {
    public SwordItem(ToolItemSettings settings, ToolItemType toolItemType, Material material) {
        super(settings, toolItemType, material);
    }

    @Override
    public boolean canApplyEnchantment(Enchantment enchantment) {
        return enchantment.type == EnchantmentTarget.WEAPON ||
                enchantment.type.isAcceptableItem(this);
    }

    @Override
    protected boolean isCorrectToolForBlock(BlockState state) {
        return state.isOf(Blocks.COBWEB);
    }
}
