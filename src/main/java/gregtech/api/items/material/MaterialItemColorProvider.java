package gregtech.api.items.material;

import gregtech.api.unification.material.Material;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;

public enum MaterialItemColorProvider implements ItemColorProvider {
    INSTANCE;

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        MaterialItem materialItem = (MaterialItem) stack.getItem();
        Material material = materialItem.getMaterial();

        if (tintIndex == 0) {
            return material.materialRGB;
        }
        return 0xFFFFFF;
    }
}
