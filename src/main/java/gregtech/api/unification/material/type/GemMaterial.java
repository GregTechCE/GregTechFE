package gregtech.api.unification.material.type;

import com.google.common.collect.ImmutableList;
import gregtech.api.unification.Element;
import gregtech.api.unification.material.properties.MaterialComponent;
import gregtech.api.unification.material.MaterialIconSet;

import static gregtech.api.unification.material.type.DustMaterial.MatFlags.GENERATE_PLATE;

//@ZenClass("mods.gregtech.material.GemMaterial")
//@ZenRegister
public class GemMaterial extends SolidMaterial {

    public static final class MatFlags {

        /**
         * If this material is crystallisable
         */
//        public static final long CRYSTALLISABLE = createFlag(34);

//        public static final long GENERATE_LENS = createFlag(37);

//        public static final long HIGH_SIFTER_OUTPUT = createFlag(38);

        static {
            Material.MatFlags.registerMaterialFlagsHolder(MatFlags.class, GemMaterial.class);
        }
    }

    public GemMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, Element element, float toolSpeed, float attackDamage, int toolDurability) {
        super(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, toolSpeed, attackDamage, toolDurability);
    }

    public GemMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, float toolSpeed, float attackDamage, int toolDurability) {
        super(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, toolSpeed, attackDamage, toolDurability);
    }

    public GemMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags) {
        super(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, 0, 0, 0);
    }

//    @Override
//    protected long verifyMaterialBits(long generationBits) {
//        if ((generationBits & MatFlags.GENERATE_LENS) > 0) {
//            generationBits |= GENERATE_PLATE;
//        }
//        return super.verifyMaterialBits(generationBits);
//    }
}

