package gregtech.api.unification.ore;

import com.google.common.base.MoreObjects;
import gregtech.api.unification.material.Material;

import java.util.Objects;

public class OreBlockType {

    private final OreVariant variant;
    private final Material material;

    public OreBlockType(OreVariant variant, Material material) {
        this.variant = variant;
        this.material = material;
    }

    public OreVariant getVariant() {
        return variant;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OreBlockType that = (OreBlockType) o;
        return variant.equals(that.variant) && material.equals(that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variant, material);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variant", variant)
                .add("material", material)
                .toString();
    }
}
