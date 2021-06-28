package gregtech.api.items.toolitem;

import gregtech.api.unification.material.type.Material;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;

public enum ToolItemColorProvider implements ItemColorProvider {
    INSTANCE;

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (stack.getItem() instanceof ToolItem toolItem) {
            Material material = toolItem.material;
            return tintIndex % 2 == 1 ? material.materialRGB : 0xFFFFFF;
        }
        return 0xFFFFFF;
    }
}
