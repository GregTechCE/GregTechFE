package gregtech.api.unification.material.type;

import com.google.common.collect.ImmutableList;
import gregtech.api.unification.material.properties.MaterialComponent;
import gregtech.api.unification.material.MaterialIconSet;
import gregtech.api.unification.ore.MaterialForm;

//@ZenClass("mods.gregtech.material.RoughSolidMaterial")
//@ZenRegister
public class RoughSolidMaterial extends SolidMaterial {

    public final MaterialForm solidForm;

    public RoughSolidMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, float toolSpeed, float attackDamage, int toolDurability, MaterialForm solidForm) {
        super(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, toolSpeed, attackDamage, toolDurability);
        this.solidForm = solidForm;
    }

    public RoughSolidMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, MaterialForm solidForm) {
        super(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, 0, 0, 0);
        this.solidForm = solidForm;
    }

}
