package gregtech.api.unification.material.type;

import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import gregtech.api.GTValues;
import gregtech.api.unification.Element;
import gregtech.api.unification.material.MaterialComponent;
import gregtech.api.unification.material.MaterialIconSet;
import gregtech.api.util.GTUtility;

//@ZenClass("mods.gregtech.material.FluidMaterial")
//@ZenRegister
public class FluidMaterial extends Material {

    public static final class MatFlags {
        /**
         * Whenever system should generate fluid block for this fluid material
         * Renamed to GENERATE_FLUID_BLOCK to avoid confusion with dust blocks
         */
        public static final long GENERATE_FLUID_BLOCK = GTUtility.createFlag(44);

        /**
         * Add this flag to enable plasma generation for this material
         */
        public static final long GENERATE_PLASMA = GTUtility.createFlag(9);

        /**
         * Marks material state as gas
         * Examples: Air, Argon, Refinery Gas, Oxygen, Hydrogen
         */
        public static final long STATE_GAS = GTUtility.createFlag(10);

        static {
            Material.MatFlags.registerMaterialFlagsHolder(MatFlags.class, FluidMaterial.class);
        }
    }

    /**
     * Internal material fluid field
     */
    //@Nullable
    private FluidKey materialFluid;

    /**
     * Internal material plasma fluid field
     */
    //@Nullable
    private FluidKey materialPlasma;

    private int fluidTemperature = 300;

    public FluidMaterial(int materialRGB, MaterialIconSet materialIconSet, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags, Element element) {
        super(materialRGB, materialIconSet, materialComponents, materialGenerationFlags, element);
    }

    public FluidMaterial(int materialRGB, MaterialIconSet materialIconSet, ImmutableList<MaterialComponent> materialComponents, long materialGenerationFlags) {
        super(materialRGB, materialIconSet, materialComponents, materialGenerationFlags, null);
    }

    //@ZenGetter("hasFluid")
    public boolean shouldGenerateFluid() {
        return true;
    }

    //@ZenGetter("hasPlasma")
    public boolean shouldGeneratePlasma() {
        return shouldGenerateFluid() && hasFlag(MatFlags.GENERATE_PLASMA);
    }

    //@ZenGetter("isGaseous")
    public boolean isGas() {
        return hasFlag(MatFlags.STATE_GAS);
    }

    @Deprecated
    public final void setMaterialFluid(FluidKey materialFluid) {
        Preconditions.checkNotNull(materialFluid);
        this.materialFluid = materialFluid;
    }

    @Deprecated
    public final void setMaterialPlasma(FluidKey materialPlasma) {
        Preconditions.checkNotNull(materialPlasma);
        this.materialPlasma = materialPlasma;
    }

    public final FluidKey getFluid() {
        return materialFluid;
    }

    public final FluidKey getPlasma() {
        return materialPlasma;
    }

    //@ZenMethod("setFluidTemperature")
    public FluidMaterial setFluidTemperature(int fluidTemperature) {
        Preconditions.checkArgument(fluidTemperature > 0, "Invalid temperature");
        this.fluidTemperature = fluidTemperature;
        return this;
    }

    //@ZenGetter("fluidTemperature")
    public int getFluidTemperature() {
        return fluidTemperature;
    }

    //@ZenGetter("fluid")
    //@Method(modid = GTValues.MODID_CT)
    //@Nullable
    //public ILiquidDefinition ctGetFluid() {
    //    Fluid materialFluid = getMaterialFluid();
    //    return materialFluid == null ? null : CraftTweakerMC.getILiquidDefinition(materialFluid);
    //}

    //@ZenGetter("plasma")
    //@Method(modid = GTValues.MODID_CT)
    //@Nullable
    //public ILiquidDefinition ctGetPlasma() {
    //    Fluid materialFluid = getMaterialPlasma();
    //    return materialFluid == null ? null : CraftTweakerMC.getILiquidDefinition(materialFluid);
    //}
}
