package gregtech.api.unification.material.properties;

import gregtech.api.unification.material.type.Material;
import gregtech.api.util.SmallDigits;

public class MaterialComponent {

    private final Material material;
    private final int amount;

    public MaterialComponent(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public static MaterialComponent of(Material material, int amount) {
        return new MaterialComponent(material, amount);
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

    public String toFormulaString() {
        String string = "";
        if (material.chemicalFormula.isEmpty()) {
            string += "?";
        } else if (material.materialComponents.size() > 1) {
            string += '(' + material.chemicalFormula + ')';
        } else {
            string += material.chemicalFormula;
        }
        if (amount > 1) {
            string += SmallDigits.toSmallDownNumbers(Long.toString(amount));
        }
        return string;
    }
}