package gregtech.api.item.util;

import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

import java.util.Set;

public interface AutoTaggedItem {

    void addItemTags(Set<Tag.Identified<Item>> outTags);
}
