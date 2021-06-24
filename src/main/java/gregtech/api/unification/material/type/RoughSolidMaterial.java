package gregtech.api.unification.material.type;

import com.google.common.collect.ImmutableList;
import gregtech.api.unification.material.MaterialComponent;
import gregtech.api.unification.material.MaterialIconSet;
import gregtech.api.unification.ore.OrePrefix;

//@ZenClass("mods.gregtech.material.RoughSolidMaterial")
//@ZenRegister
public class RoughSolidMaterial extends SolidMaterial {

    public final OrePrefix solidForm;

    public RoughSolidMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, float toolSpeed, float attackDamage, int toolDurability, OrePrefix solidForm) {
        super(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, toolSpeed, attackDamage, toolDurability);
        this.solidForm = solidForm;
    }

    public RoughSolidMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, OrePrefix solidForm) {
        super(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, 0, 0, 0);
        this.solidForm = solidForm;
    }

}
