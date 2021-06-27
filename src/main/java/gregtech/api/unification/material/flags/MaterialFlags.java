package gregtech.api.unification.material.flags;

public class MaterialFlags {

    /**
     * Add to material if it is some kind of explosive
     */
    public static final MaterialFlag EXPLOSIVE = new MaterialFlag.Builder("EXPLOSIVE").build();

    /**
     * Not used
     */
    public static final MaterialFlag NO_UNIFICATION = new MaterialFlag.Builder("NO_UNIFICATION").build();

    /**
     * Add to material if any of it's items cannot be recycled to get scrub
     * Not used
     */
    public static final MaterialFlag NO_RECYCLING = new MaterialFlag.Builder("NO_RECYCLING").build();

    /**
     * Material with constantly burning aura
     * Not used
     */
    public static final MaterialFlag BURNING = new MaterialFlag.Builder("BURNING").build();

    /**
     * Decomposition recipe requires hydrogen as additional input. Amount is equal to input amount
     */
    public static final MaterialFlag DECOMPOSITION_REQUIRES_HYDROGEN = new MaterialFlag.Builder("DECOMPOSITION_REQUIRES_HYDROGEN").build();

    /**
     * Add this flag to enable plasma generation for this material
     */
    public static final MaterialFlag GENERATE_PLASMA = new MaterialFlag.Builder("GENERATE_PLASMA").build();

    /**
     * Marks material state as gas
     * Examples: Air, Argon, Refinery Gas, Oxygen, Hydrogen
     */
    public static final MaterialFlag STATE_GAS = new MaterialFlag.Builder("STATE_GAS").build();

    public static final MaterialFlag GENERATE_ORE = new MaterialFlag.Builder("GENERATE_ORE").build();

    /**
     * Generate a plate for this material
     * If it's dust material, dust compressor recipe into plate will be generated
     * If it's metal material, bending machine recipes will be generated
     * If block is found, cutting machine recipe will be also generated
     */
    public static final MaterialFlag GENERATE_PLATE = new MaterialFlag.Builder("GENERATE_PLATE").build();

    /**
     * Add to material if it cannot be worked by any other means, than smashing or smelting. This is used for coated Materials.
     */
    public static final MaterialFlag NO_WORKING = new MaterialFlag.Builder("NO_WORKING").build();

    /**
     * Add to material if it cannot be used for regular Metal working techniques since it is not possible to bend it.
     */
    public static final MaterialFlag NO_SMASHING = new MaterialFlag.Builder("NO_SMASHING").build();

    /**
     * Add to material if it's impossible to smelt it
     */
    public static final MaterialFlag NO_SMELTING = new MaterialFlag.Builder("NO_SMELTING").build();

    /**
     * Add to material if it is outputting less in an Induction Smelter.
     */
    public static final MaterialFlag INDUCTION_SMELTING_LOW_OUTPUT = new MaterialFlag.Builder("INDUCTION_SMELTING_LOW_OUTPUT").build();

    /**
     * Add to material if it melts into fluid (and it will also generate fluid for this material)
     */
    public static final MaterialFlag SMELT_INTO_FLUID = new MaterialFlag.Builder("SMELT_INTO_FLUID").build();

    /**
     * This will prevent material from creating Shapeless recipes for dust to block and vice versa
     * Also preventing extruding and alloy smelting recipes via SHAPE_EXTRUDING/MOLD_BLOCK
     */
    public static final MaterialFlag EXCLUDE_BLOCK_CRAFTING_RECIPES = new MaterialFlag.Builder("EXCLUDE_BLOCK_CRAFTING_RECIPES").build();

    /**
     * Material will not generate recipe for Plate like item in Compressor
     */
    public static final MaterialFlag EXCLUDE_PLATE_COMPRESSOR_RECIPE = new MaterialFlag.Builder("EXCLUDE_PLATE_COMPRESSOR_RECIPE").build();

    public static final MaterialFlag GENERATE_ROD = new MaterialFlag.Builder("GENERATE_ROD").build();

    public static final MaterialFlag GENERATE_GEAR = new MaterialFlag.Builder("GENERATE_GEAR").requiresFlag(GENERATE_PLATE).requiresFlag(GENERATE_ROD).build();

    public static final MaterialFlag GENERATE_LONG_ROD = new MaterialFlag.Builder("GENERATE_LONG_ROD").requiresFlag(GENERATE_ROD).build();

    public static final MaterialFlag MORTAR_GRINDABLE = new MaterialFlag.Builder("MORTAR_GRINDABLE").build();

    public static final MaterialFlag GENERATE_FOIL = new MaterialFlag.Builder("GENERATE_FOIL").requiresFlag(GENERATE_PLATE).build();

    public static final MaterialFlag GENERATE_BOLT_SCREW = new MaterialFlag.Builder("GENERATE_BOLT_SCREW").requiresFlag(GENERATE_ROD).build();

    public static final MaterialFlag GENERATE_RING = new MaterialFlag.Builder("GENERATE_RING").requiresFlag(GENERATE_ROD).build();

    public static final MaterialFlag GENERATE_SPRING = new MaterialFlag.Builder("GENERATE_SPRING").build();

    public static final MaterialFlag GENERATE_FINE_WIRE = new MaterialFlag.Builder("GENERATE_FINE_WIRE").requiresFlag(GENERATE_FOIL).build();

    public static final MaterialFlag GENERATE_ROTOR = new MaterialFlag.Builder("GENERATE_ROTOR").requiresFlag(GENERATE_BOLT_SCREW).requiresFlag(GENERATE_RING).requiresFlag(GENERATE_PLATE).build();

    public static final MaterialFlag GENERATE_SMALL_GEAR = new MaterialFlag.Builder("GENERATE_SMALL_GEAR").requiresFlag(GENERATE_PLATE).build();

    public static final MaterialFlag GENERATE_DENSE = new MaterialFlag.Builder("GENERATE_DENSE").requiresFlag(GENERATE_PLATE).build();

    /**
     * Not used - Small spring as type exists but is not generated or used
     */
    public static final MaterialFlag GENERATE_SPRING_SMALL = new MaterialFlag.Builder("GENERATE_SPRING_SMALL").build();

    /**
     * If this material is crystallise-able
     */
    public static final MaterialFlag CRYSTALLISABLE = new MaterialFlag.Builder("CRYSTALLISABLE").build();

    /**
     * This is for both BLAST_FURNACE_CALCITE_*
     * Add this to your Material if you want to have its Ore Calcite heated in a Blast Furnace for more output. Already listed are:
     * Iron, Pyrite, PigIron, WroughtIron.
     * Not used - flag is added but nothing is reacting to it
     */
    public static final MaterialFlag BLAST_FURNACE_CALCITE_DOUBLE = new MaterialFlag.Builder("BLAST_FURNACE_CALCITE_DOUBLE").build();
    public static final MaterialFlag BLAST_FURNACE_CALCITE_TRIPLE = new MaterialFlag.Builder("BLAST_FURNACE_CALCITE_TRIPLE").build();

    public static final MaterialFlag GENERATE_LENS = new MaterialFlag.Builder("GENERATE_LENS").requiresFlag(GENERATE_PLATE).build();

    public static final MaterialFlag HIGH_SIFTER_OUTPUT = new MaterialFlag.Builder("HIGH_SIFTER_OUTPUT").build();

    /**
     * Enables electrolyzer decomposition recipe generation
     */
    public static final MaterialFlag DECOMPOSITION_BY_ELECTROLYZING = new MaterialFlag.Builder("DECOMPOSITION_BY_ELECTROLYZING").build();

    /**
     * Enables centrifuge decomposition recipe generation
     */
    public static final MaterialFlag DECOMPOSITION_BY_CENTRIFUGING = new MaterialFlag.Builder("DECOMPOSITION_BY_CENTRIFUGING").build();

    /**
     * Add to material if it is some kind of flammable
     */
    public static final MaterialFlag FLAMMABLE = new MaterialFlag.Builder("FLAMMABLE").build();

    /**
     * Disables decomposition recipe generation for this material and all materials that has it as component
     */
    public static final MaterialFlag DISABLE_DECOMPOSITION = new MaterialFlag.Builder("DISABLE_DECOMPOSITION").build();

    /**
     * Whenever system should generate fluid block for this fluid material
     */
    public static final MaterialFlag GENERATE_FLUID_BLOCK = new MaterialFlag.Builder("GENERATE_FLUID_BLOCK").build();

    public static final MaterialFlag GENERATE_FRAME = new MaterialFlag.Builder("GENERATE_FRAME").build();

    /**
     * This will prevent material from creating Shapeless recipes for dust to block and vice versa
     */
    public static final MaterialFlag EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES = new MaterialFlag.Builder("EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES").build();


    private MaterialFlags() {

    }
}
