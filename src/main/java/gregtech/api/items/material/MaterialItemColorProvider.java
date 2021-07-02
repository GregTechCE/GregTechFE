package gregtech.api.items.material;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.MaterialFlags;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;

public enum MaterialItemColorProvider implements ItemColorProvider {
    INSTANCE;

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 0 && stack.getItem() instanceof MaterialItem materialItem) {
            Material material = materialItem.getMaterial();
            return material.queryPropertyChecked(MaterialFlags.COLOR);
        }
        return 0xFFFFFF;
    }
}
