package gregtech.api.unification.material.properties;

import com.google.common.base.Preconditions;
import gregtech.api.unification.material.Material;

public class MaterialComponent {

    private final Material material;
    private final int amount;

    private MaterialComponent(Material material, int amount) {
        Preconditions.checkNotNull(material);
        this.material = material;
        this.amount = amount;
    }

    public static MaterialComponent of(Material material, int amount) {
        return new MaterialComponent(material, amount);
    }

    public static MaterialComponent of(Material material) {
        return new MaterialComponent(material, 1);
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%dx%s", amount, material);
    }

}
