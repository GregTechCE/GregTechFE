package gregtech.api.items.material;

import gregtech.api.unification.material.type.Material;

import java.util.Objects;

public class MaterialItemId {

    private final MaterialItemForm form;
    private final Material material;

    public MaterialItemId(MaterialItemForm form, Material material) {
        this.form = form;
        this.material = material;
    }

    public MaterialItemForm getForm() {
        return form;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialItemId that = (MaterialItemId) o;
        return Objects.equals(form, that.form) && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(form, material);
    }
}
