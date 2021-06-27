package gregtech.api.unification.material;

import gregtech.api.unification.material.type.Material;
import gregtech.api.util.SmallDigits;

//@ZenClass("mods.gregtech.material.MaterialStack")
//@ZenRegister
//TODO: Remove this
public class MaterialComponent {

    //@ZenProperty
    public final Material material;
    //@ZenProperty
    public final int amount;

    public MaterialComponent(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    //@ZenMethod
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