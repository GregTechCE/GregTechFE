package gregtech.common.tools;
import gregtech.api.items.toolitem.MiningToolItem;
import gregtech.api.items.toolitem.ToolItemSettings;
import gregtech.api.items.toolitem.ToolItemType;
import gregtech.api.unification.material.Material;
import net.minecraft.tag.BlockTags;

public class PickaxeItem extends MiningToolItem {

    public PickaxeItem(ToolItemSettings settings, ToolItemType toolItemType, Material material) {
        super(settings, toolItemType, material, BlockTags.PICKAXE_MINEABLE);
    }
}
