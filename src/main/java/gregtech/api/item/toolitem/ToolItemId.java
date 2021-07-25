package gregtech.api.item.toolitem;

import com.google.common.base.MoreObjects;
import gregtech.api.unification.material.Material;

import java.util.Objects;

public class ToolItemId {

    private final ToolItemType itemType;
    private final Material material;

    public ToolItemId(ToolItemType itemType, Material material) {
        this.itemType = itemType;
        this.material = material;
    }

    public ToolItemType getItemType() {
        return itemType;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("itemType", itemType)
                .add("material", material)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolItemId that = (ToolItemId) o;
        return Objects.equals(itemType, that.itemType) && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemType, material);
    }
}
