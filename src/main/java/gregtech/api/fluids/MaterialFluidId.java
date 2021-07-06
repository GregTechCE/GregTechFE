package gregtech.api.fluids;

import com.google.common.base.MoreObjects;
import gregtech.api.unification.material.Material;

import java.util.Objects;

public class MaterialFluidId {

    private final MaterialFluidKind kind;
    private final Material material;

    public MaterialFluidId(MaterialFluidKind kind, Material material) {
        this.kind = kind;
        this.material = material;
    }

    public MaterialFluidKind getKind() {
        return kind;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("kind", kind)
                .add("material", material)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialFluidId that = (MaterialFluidId) o;
        return kind == that.kind && material.equals(that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, material);
    }
}
