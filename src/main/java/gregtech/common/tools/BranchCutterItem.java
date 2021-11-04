package gregtech.common.tools;

import gregtech.api.items.toolitem.MiningToolItem;
import gregtech.api.items.toolitem.ToolItemSettings;
import gregtech.api.items.toolitem.ToolItemType;
import gregtech.api.unification.material.Material;
import net.minecraft.tag.BlockTags;

public class BranchCutterItem extends MiningToolItem {
    // TODO: Do either 100% sapling drop without forestry, capitator-like leaves cutting or remove entirely
    public BranchCutterItem(ToolItemSettings settings, ToolItemType toolItemType, Material material) {
        super(settings, toolItemType, material, BlockTags.LEAVES);
    }
}
