package gregtech.api.block.ore;

import gregtech.api.items.util.AutoTaggedItem;
import gregtech.api.unification.forms.MaterialForms;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;

import java.util.Set;

public class OreBlockItem extends BlockItem implements AutoTaggedItem {

    private final OreBlock oreBlock;

    public OreBlockItem(Settings settings, OreBlock block) {
        super(block, settings);
        this.oreBlock = block;
    }

    public OreBlock getOreBlock() {
        return oreBlock;
    }

    @Override
    public Text getName() {
        return this.oreBlock.getName();
    }

    @Override
    public Text getName(ItemStack stack) {
        return this.oreBlock.getName();
    }

    @Override
    public void addItemTags(Set<Tag.Identified<Item>> outTags) {
        outTags.add(MaterialForms.ORES.getItemTag(oreBlock.getMaterial()));
    }
}
