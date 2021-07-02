package gregtech.api.block.util;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

import java.util.Set;

public interface AutoTaggedBlock {

    void addBlockTags(Set<Tag.Identified<Block>> outTags);
}
