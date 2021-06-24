package gregtech.api.items.material;

import gregtech.api.unification.material.type.Material;
import net.minecraft.item.Item;

public class MaterialItem extends Item {

    private final MaterialItemForm itemForm;
    private final Material material;

    public MaterialItem(Settings settings, MaterialItemForm itemForm, Material material) {
        super(settings);
        this.itemForm = itemForm;
        this.material = material;
    }
}
