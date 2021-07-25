package gregtech.api.item.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

public interface ItemEntityAwareItem {

    void onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity);
}
