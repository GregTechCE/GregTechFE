package gregtech.api.items.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

public interface ItemEntityAwareItem {

    void onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity);
}
