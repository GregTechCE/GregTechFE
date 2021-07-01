package gregtech.api.unification.material.flags;

import gregtech.api.GTValues;
import gregtech.api.unification.util.MaterialIconSet;
import gregtech.api.unification.util.MaterialIconSets;
import gregtech.api.unification.material.properties.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialFlags {

    //MATERIAL PROPERTIES

    /** Color of the material, in 0xRRGGBB format. Material parts will be tinted with this value */
    public static final MaterialProperty<Integer> COLOR;

    /** Set of icons used for items generated for this material */
    public static final MaterialProperty<MaterialIconSet> ICON_SET;

    /** Chemical composition of the material, used for formula generation and recipe duration adjustment + decomposition */
    public static final MaterialProperty<ChemicalComposition> CHEMICAL_COMPOSITION;

    /** Harvest level of this material, e.g. of it's blocks and tools */
    public static final MaterialProperty<Integer> HARVEST_LEVEL;

    /** Solid form of this material, causes generation of either ingot or a gem */
    public static final MaterialProperty<SolidForm> SOLID_FORM;

    /** Properties tools made of this material have. Forces generation of tools */
    public static final MaterialProperty<ToolProperties> TOOL_PROPERTIES;

    /** Burn time of one dust of the material, in minecraft ticks */
    public static final MaterialProperty<Integer> BURN_TIME;

    /** Properties enabling fluid generation and specifying it's attributes */
    public static final MaterialProperty<FluidProperties> FLUID_PROPERTIES;

    /** Rules for decomposition recipe generation for this material. These recipes allow breaking materials into their compounds */
    public static final MaterialProperty<DecompositionProperty> DECOMPOSITION_PROPERTY;

    /** Properties specifying ore processing rules and enabling it's block generation */
    public static final MaterialProperty<OreProperties> ORE_PROPERTIES;

    /** Marks material as magnetic and points to it's respective polarized/demagnetized version */
    public static final MaterialProperty<PolarizableMetalProperty> POLARIZABLE_METAL;

    /** Specifies blast furnace temperature required to smelt this material. Values above 1750K cause hot ingot generation too */
    public static final MaterialProperty<Integer> BLAST_FURNACE_TEMPERATURE;

    /** Specifies material into which this one is turned when smelt in arc furnace */
    public static final MaterialProperty<ArcSmeltProperty> ARC_SMELT_PROPERTY;

    /** Properties cable made out of this material possesses. Material should be a metal. */
    public static final MaterialProperty<CableProperties> CABLE_PROPERTIES;

    // GENERIC MATERIAL FLAGS

    /** Material should generate dust item and a block (unless DISABLE_BLOCK) is specified */
    public static final MaterialFlag GENERATE_DUST;

    /** Material is flammable and can (optionally) have furnace burn time defined */
    public static final MaterialFlag FLAMMABLE;

    /** Material is explosive and cannot be compressed in implosion compressor */
    public static final MaterialFlag EXPLOSIVE;

    /** Forces Plasma fluid generation for this material. Material must have fluid properties defined. */
    public static final MaterialFlag GENERATE_PLASMA;

    // RECIPE GENERATION RELATED FLAGS

    /** Disables automatic block generation for this material, as well as any related recipes */
    public static final MaterialFlag DISABLE_BLOCK;

    /** Allows this material to be crystallised in the autoclave */
    public static final MaterialFlag CRYSTALLISABLE;

    /** Material can be grinded in the mortar */
    public static final MaterialFlag MORTAR_GRINDABLE;

    /** Material has a high sifting output */
    public static final MaterialFlag HIGH_SIFTER_OUTPUT;

    // MATERIAL ITEM FORM GENERATION FLAGS

    public static final MaterialFlag GENERATE_PLATE;

    public static final MaterialFlag GENERATE_DENSE_PLATE;
    public static final MaterialFlag GENERATE_FOIL;
    public static final MaterialFlag GENERATE_FINE_WIRE;

    public static final MaterialFlag GENERATE_ROD;
    public static final MaterialFlag GENERATE_LONG_ROD;

    public static final MaterialFlag GENERATE_BOLT_SCREW;
    public static final MaterialFlag GENERATE_RING;

    public static final MaterialFlag GENERATE_GEAR;
    public static final MaterialFlag GENERATE_SMALL_GEAR;

    public static final MaterialFlag GENERATE_SPRING;
    public static final MaterialFlag GENERATE_SPRING_SMALL;

    public static final MaterialFlag GENERATE_LENS;

    // INITIALIZATION

    private static <T extends MaterialFlag> T register(String name, T flag) {
        return Registry.register(MaterialFlag.REGISTRY, new Identifier(GTValues.MODID, name), flag);
    }

    static {
        COLOR = register("color", new MaterialProperty<>(
                new MaterialProperty.Settings<Integer>()
                        .valueType(Integer.class)
                        .defaultValue(0xFFFFFF)
        ));

        ICON_SET = register("icon_set", new MaterialProperty<>(
                new MaterialProperty.Settings<MaterialIconSet>()
                        .valueType(MaterialIconSet.class)
                        .defaultValue(MaterialIconSets.METALLIC)
        ));

        CHEMICAL_COMPOSITION = register("chemical_composition", new MaterialProperty<>(
                new MaterialProperty.Settings<ChemicalComposition>()
                        .valueType(ChemicalComposition.class)
                        .defaultValue(ChemicalComposition.EMPTY)
        ));

        GENERATE_DUST = register("generate_dust", new MaterialFlag(
                new MaterialFlag.Settings()
        ));

        HARVEST_LEVEL = register("harvest_level", new MaterialProperty<>(
                new MaterialProperty.Settings<Integer>()
                        .valueType(Integer.class)
                        .defaultValue(0)
                        .requires(GENERATE_DUST)
        ));

        SOLID_FORM = register("solid_form", new MaterialProperty<>(
                new MaterialProperty.Settings<SolidForm>()
                        .valueType(SolidForm.class)
                        .requires(GENERATE_DUST)
        ));

        TOOL_PROPERTIES = register("tool_properties", new MaterialProperty<>(
                new MaterialProperty.Settings<ToolProperties>()
                        .valueType(ToolProperties.class)
                        .requires(HARVEST_LEVEL)
                        .requires(SOLID_FORM)
        ));

        FLAMMABLE = register("flammable", new MaterialFlag(
                new MaterialFlag.Settings()
        ));

        BURN_TIME = register("burn_time", new MaterialProperty<>(
                new MaterialProperty.Settings<Integer>()
                        .requires(FLAMMABLE)
                        .requires(GENERATE_DUST)
        ));

        FLUID_PROPERTIES = register("fluid_properties", new MaterialProperty<>(
                new MaterialProperty.Settings<FluidProperties>()
                        .valueType(FluidProperties.class)
        ));

        DECOMPOSITION_PROPERTY = register("decomposition_property", new MaterialProperty<>(
                new MaterialProperty.Settings<DecompositionProperty>()
                        .valueType(DecompositionProperty.class)
                        .requiresEither(GENERATE_DUST, FLUID_PROPERTIES)
        ));

        ORE_PROPERTIES = register("ore_properties", new MaterialProperty<>(
                new MaterialProperty.Settings<OreProperties>()
                    .valueType(OreProperties.class)
                    .requires(HARVEST_LEVEL)
                    .requires(GENERATE_DUST)
        ));

        POLARIZABLE_METAL = register("polarizable_metal", new MaterialProperty<>(
                new MaterialProperty.Settings<PolarizableMetalProperty>()
                    .valueType(PolarizableMetalProperty.class)
                    .requires(SOLID_FORM, SolidForm.METAL)
        ));

        BLAST_FURNACE_TEMPERATURE = register("blast_furnace_temperature", new MaterialProperty<>(
                new MaterialProperty.Settings<Integer>()
                    .valueType(Integer.class)
                    .requires(SOLID_FORM, SolidForm.METAL)
        ));

        ARC_SMELT_PROPERTY = register("arc_smelt_property", new MaterialProperty<>(
                new MaterialProperty.Settings<ArcSmeltProperty>()
                    .valueType(ArcSmeltProperty.class)
                    .requires(SOLID_FORM, SolidForm.METAL)
        ));

        CABLE_PROPERTIES = register("cable_properties", new MaterialProperty<>(
                new MaterialProperty.Settings<CableProperties>()
                    .valueType(CableProperties.class)
                    .requires(SOLID_FORM, SolidForm.METAL)
        ));

        EXPLOSIVE = register("explosive", new MaterialFlag(
                new MaterialFlag.Settings()
        ));

        GENERATE_PLASMA = register("generate_plasma", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(FLUID_PROPERTIES)
        ));

        DISABLE_BLOCK = register("disable_block", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(GENERATE_DUST)
        ));

        CRYSTALLISABLE = register("crystallisable", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.GEM)
        ));

        MORTAR_GRINDABLE = register("mortar_grindable", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.METAL)
        ));

        HIGH_SIFTER_OUTPUT = register("high_sifter_output", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.GEM)
                    .requires(ORE_PROPERTIES)
        ));

        GENERATE_PLATE = register("generate_plate", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(GENERATE_DUST)
        ));

        GENERATE_DENSE_PLATE = register("generate_dense_plate", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.METAL)
                    .requires(GENERATE_PLATE)
        ));

        GENERATE_FOIL = register("generate_foil", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.METAL)
                    .requires(GENERATE_PLATE)
        ));

        GENERATE_FINE_WIRE = register("generate_fine_wire", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.METAL)
                    .requires(CABLE_PROPERTIES)
        ));

        GENERATE_ROD = register("generate_rod", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requiresPredicate(SOLID_FORM, SolidForm::isMetalOrGem)
        ));

        GENERATE_LONG_ROD = register("generate_long_rod", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requiresPredicate(SOLID_FORM, SolidForm::isMetalOrGem)
                    .requires(GENERATE_ROD)
        ));

        GENERATE_BOLT_SCREW = register("generate_bolt_screw", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.METAL)
                    .requires(GENERATE_ROD)
        ));

        GENERATE_RING = register("generate_ring", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM)
                    .requiresEither(GENERATE_ROD, GENERATE_PLATE)
        ));

        GENERATE_GEAR = register("generate_gear", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requiresPredicate(SOLID_FORM, SolidForm::isMetalOrGem)
                    .requires(GENERATE_PLATE)
                    .requires(GENERATE_ROD)
        ));

        GENERATE_SMALL_GEAR = register("generate_small_gear", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.METAL)
                    .requires(GENERATE_PLATE)
        ));

        GENERATE_SPRING = register("generate_spring", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.METAL)
                    .requires(GENERATE_LONG_ROD)
        ));

        GENERATE_SPRING_SMALL = register("generate_spring_small", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.METAL)
                    .requires(CABLE_PROPERTIES)
        ));

        GENERATE_LENS = register("generate_lens", new MaterialFlag(
                new MaterialFlag.Settings()
                    .requires(SOLID_FORM, SolidForm.GEM)
                    .requires(GENERATE_PLATE)
        ));
    }
}
