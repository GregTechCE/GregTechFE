package gregtech.api.unification.material.type;

import com.google.common.collect.ImmutableList;
import gregtech.api.unification.Element;
import gregtech.api.unification.material.properties.MaterialComponent;
import gregtech.api.unification.material.MaterialIconSet;
import org.jetbrains.annotations.Nullable;

import static gregtech.api.unification.material.type.DustMaterial.MatFlags.GENERATE_PLATE;
import static gregtech.api.unification.material.type.DustMaterial.MatFlags.SMELT_INTO_FLUID;
import static gregtech.api.unification.material.type.SolidMaterial.MatFlags.GENERATE_ROD;

//@ZenClass("mods.gregtech.material.IngotMaterial")
//@ZenRegister
public class IngotMaterial extends SolidMaterial {

    public static final class MatFlags {

//        public static final long GENERATE_FOIL = createFlag(25);
//        public static final long GENERATE_BOLT_SCREW = createFlag(26);
//        public static final long GENERATE_RING = createFlag(27);
//        public static final long GENERATE_SPRING = createFlag(28);
//        public static final long GENERATE_FINE_WIRE = createFlag(29);
//        public static final long GENERATE_ROTOR = createFlag(30);
//        public static final long GENERATE_SMALL_GEAR = createFlag(31);
//        public static final long GENERATE_DENSE = createFlag(32);
//        public static final long GENERATE_SPRING_SMALL = createFlag(33);

        /**
         * Add this to your Material if you want to have its Ore Calcite heated in a Blast Furnace for more output. Already listed are:
         * Iron, Pyrite, PigIron, WroughtIron.
         */
//        public static final long BLAST_FURNACE_CALCITE_DOUBLE = createFlag(35);
//        public static final long BLAST_FURNACE_CALCITE_TRIPLE = createFlag(36);

        static {
            Material.MatFlags.registerMaterialFlagsHolder(MatFlags.class, IngotMaterial.class);
        }
    }

    /**
     * Specifies a material into which this material parts turn when heated
     */
    public IngotMaterial smeltInto;

    /**
     * Specifies a material into which this material parts turn when heated in arc furnace
     */
    public IngotMaterial arcSmeltInto;

    /**
     * Material which obtained when this material is polarized. Can be null.
     */
    @Nullable
    public IngotMaterial magneticMaterial;

    /**
     * Blast furnace temperature of this material
     * Equal to zero if material doesn't use blast furnace
     * If below 1000K, primitive blast furnace recipes will be also added.
     * If above 1750K, a Hot Ingot and its Vacuum Freezer recipe will be also added.
     */
    //@ZenProperty
    public final int blastFurnaceTemperature;

    public IngotMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, Element element, float toolSpeed, float attackDamage, int toolDurability, int blastFurnaceTemperature) {
        super(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, toolSpeed, attackDamage, toolDurability);
        this.blastFurnaceTemperature = blastFurnaceTemperature;
        this.smeltInto = this;
        this.arcSmeltInto = this;
        addFlag(SMELT_INTO_FLUID);
    }

    public IngotMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, Element element) {
        this(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, 0, 0, 0, 0);
    }

    public IngotMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, Element element, int blastFurnaceTemperature) {
        this(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, 0, 0, 0, blastFurnaceTemperature);
    }

    public IngotMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, Element element, float toolSpeed, float attackDamage, int toolDurability) {
        this(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, toolSpeed, attackDamage, toolDurability, 0);
    }

    public IngotMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, float toolSpeed, float attackDamage, int toolDurability) {
        this(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, toolSpeed, attackDamage, toolDurability, 0);
    }

    public IngotMaterial(int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags) {
        this(materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, 0, 0, 0, 0);
    }

    @Override
    protected void initializeMaterial() {
        super.initializeMaterial();
        if (blastFurnaceTemperature > 0) {
            setFluidTemperature(blastFurnaceTemperature);
        } else {
            setFluidTemperature(1273);
        }
    }

//    @Override
//    protected long verifyMaterialBits(long generationBits) {
//        if ((generationBits & GENERATE_DENSE) > 0) {
//            generationBits |= GENERATE_PLATE;
//        }
//        if ((generationBits & GENERATE_ROTOR) > 0) {
//            generationBits |= GENERATE_BOLT_SCREW;
//            generationBits |= GENERATE_RING;
//            generationBits |= GENERATE_PLATE;
//        }
//        if ((generationBits & GENERATE_SMALL_GEAR) > 0) {
//            generationBits |= GENERATE_PLATE;
//        }
//        if ((generationBits & GENERATE_FINE_WIRE) > 0) {
//            generationBits |= GENERATE_FOIL;
//        }
//        if ((generationBits & GENERATE_FOIL) > 0) {
//            generationBits |= GENERATE_PLATE;
//        }
//        if ((generationBits & GENERATE_RING) > 0) {
//            generationBits |= GENERATE_ROD;
//        }
//        if ((generationBits & GENERATE_BOLT_SCREW) > 0) {
//            generationBits |= GENERATE_ROD;
//        }
//        return super.verifyMaterialBits(generationBits);
//    }

    public void setSmeltingInto(IngotMaterial smeltInto) {
        this.smeltInto = smeltInto;
    }

    public void setArcSmeltingInto(IngotMaterial arcSmeltingInto) {
        this.arcSmeltInto = arcSmeltingInto;
    }
}
