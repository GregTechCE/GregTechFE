package gregtech.api.items.material;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialItemForms {

    //Common dust forms
    public static final MaterialItemForm DUST;
    public static final MaterialItemForm DUST_SMALL;
    public static final MaterialItemForm DUST_TINY;

    //Common ingot forms
    public static final MaterialItemForm INGOT;
    public static final MaterialItemForm INGOT_HOT;
    public static final MaterialItemForm NUGGET;

    //Common gem forms
    public static final MaterialItemForm GEM;

    public static final MaterialItemForm GEM_CHIPPED;
    public static final MaterialItemForm GEM_FLAWED;
    public static final MaterialItemForm GEM_FLAWLESS;
    public static final MaterialItemForm GEM_EXQUISITE;

    //Ore processing forms
    public static final MaterialItemForm CRUSHED;
    public static final MaterialItemForm CRUSHED_CENTRIFUGED;
    public static final MaterialItemForm CRUSHED_PURIFIED;

    public static final MaterialItemForm DUST_IMPURE;
    public static final MaterialItemForm DUST_PURE;

    //Various crafting parts
    public static final MaterialItemForm PLATE;
    public static final MaterialItemForm PLATE_DENSE;
    public static final MaterialItemForm FOIL;

    public static final MaterialItemForm STICK;
    public static final MaterialItemForm STICK_LONG;

    public static final MaterialItemForm BOLT;
    public static final MaterialItemForm SCREW;
    public static final MaterialItemForm RING;

    public static final MaterialItemForm SPRING;
    public static final MaterialItemForm SPRING_SMALL;

    public static final MaterialItemForm WIRE_FINE;

    public static final MaterialItemForm ROTOR;
    public static final MaterialItemForm GEAR;
    public static final MaterialItemForm GEAR_SMALL;

    public static final MaterialItemForm LENSE;

    private static MaterialItemForm register(String name, MaterialItemForm.Settings settings) {
        return Registry.register(MaterialItemForm.REGISTRY, new Identifier(GTValues.MODID, name), new MaterialItemForm(settings));
    }

    public static void init() {
        Preconditions.checkNotNull(LENSE);
    }

    static {
        //...
    }

    /*
    dust("Dusts", M, null, MaterialIconType.dust, ENABLE_UNIFICATION | DISALLOW_RECYCLING, mat -> mat instanceof DustMaterial), // Pure Dust worth of one Ingot or Gem. Introduced by Alblaka.

    dustSmall("Small Dusts", M / 4, null, MaterialIconType.dustSmall, ENABLE_UNIFICATION | DISALLOW_RECYCLING, mat -> mat instanceof DustMaterial), // 1/4th of a Dust.
    dustTiny("Tiny Dusts", M / 9, null, MaterialIconType.dustTiny, ENABLE_UNIFICATION | DISALLOW_RECYCLING, mat -> mat instanceof DustMaterial), // 1/9th of a Dust.


    plateDense("Dense Plates", M * 9, null, MaterialIconType.plateDense, ENABLE_UNIFICATION, mat -> mat instanceof IngotMaterial && mat.hasFlag(GENERATE_PLATE | GENERATE_DENSE) && !mat.hasFlag(NO_SMASHING)), // 9 Plates combined in one Item.
    plate("Plates", M, null, MaterialIconType.plate, ENABLE_UNIFICATION, mat -> mat instanceof DustMaterial && mat.hasFlag(GENERATE_PLATE)), // Regular Plate made of one Ingot/Dust. Introduced by Calclavia

    foil("Foils", M / 4, null, MaterialIconType.foil, ENABLE_UNIFICATION, mat -> mat instanceof IngotMaterial && mat.hasFlag(GENERATE_FOIL)), // Foil made of 1/4 Ingot/Dust.
    stickLong("Long Sticks/Rods", M, null, MaterialIconType.stickLong, ENABLE_UNIFICATION, mat -> mat instanceof SolidMaterial && mat.hasFlag(GENERATE_LONG_ROD)), // Stick made of an Ingot.
    stick("Sticks/Rods", M / 2, null, MaterialIconType.stick, ENABLE_UNIFICATION, mat -> mat instanceof SolidMaterial && mat.hasFlag(GENERATE_ROD)), // Stick made of half an Ingot. Introduced by Eloraam

    bolt("Bolts", M / 8, null, MaterialIconType.bolt, ENABLE_UNIFICATION, mat -> mat instanceof SolidMaterial && mat.hasFlag(GENERATE_BOLT_SCREW)), // consisting out of 1/8 Ingot or 1/4 Stick.
    screw("Screws", M / 9, null, MaterialIconType.screw, ENABLE_UNIFICATION, mat -> mat instanceof IngotMaterial && mat.hasFlag(GENERATE_BOLT_SCREW)), // consisting out of a Bolt.
    ring("Rings", M / 4, null, MaterialIconType.ring, ENABLE_UNIFICATION, mat -> mat instanceof SolidMaterial && mat.hasFlag(GENERATE_RING)), // consisting out of 1/2 Stick.
    springSmall("Small Springs", M / 4, null, MaterialIconType.springSmall, ENABLE_UNIFICATION, mat -> mat instanceof IngotMaterial && mat.hasFlag(GENERATE_SPRING_SMALL) && !mat.hasFlag(NO_SMASHING)), // consisting out of 1 Fine Wire.
    spring("Springs", M, null, MaterialIconType.spring, ENABLE_UNIFICATION, mat -> mat instanceof IngotMaterial && mat.hasFlag(GENERATE_SPRING) && !mat.hasFlag(NO_SMASHING)), // consisting out of 2 Sticks.
    wireFine("Fine Wires", M / 8, null, MaterialIconType.wireFine, ENABLE_UNIFICATION, mat -> mat instanceof IngotMaterial && mat.hasFlag(GENERATE_FINE_WIRE)), // consisting out of 1/8 Ingot or 1/4 Wire.
    rotor("Rotors", M * 4, null, MaterialIconType.rotor, ENABLE_UNIFICATION, mat -> mat instanceof IngotMaterial && mat.hasFlag(GENERATE_ROTOR)), // consisting out of 4 Plates, 1 Ring and 1 Screw.
    gearSmall("Small Gears", M, null, MaterialIconType.gearSmall, ENABLE_UNIFICATION, mat -> mat instanceof IngotMaterial && mat.hasFlag(GENERATE_SMALL_GEAR)),
    gear("Gears", M * 4, null, MaterialIconType.gear, ENABLE_UNIFICATION, mat -> mat instanceof SolidMaterial && mat.hasFlag(GENERATE_GEAR)), // Introduced by me because BuildCraft has ruined the gear Prefix...
    lens("Lenses", (M * 3) / 4, null, MaterialIconType.lens, ENABLE_UNIFICATION, mat -> mat instanceof GemMaterial && mat.hasFlag(GENERATE_LENS)), // 3/4 of a Plate or Gem used to shape a Lense. Normally only used on Transparent Materials.


    crushedCentrifuged("Centrifuged Ores", -1, null, MaterialIconType.crushedCentrifuged, ENABLE_UNIFICATION | DISALLOW_RECYCLING, (mat) -> mat instanceof DustMaterial && mat.hasFlag(GENERATE_ORE)),
    crushedPurified("Purified Ores", -1, null, MaterialIconType.crushedPurified, ENABLE_UNIFICATION | DISALLOW_RECYCLING, (mat) -> mat instanceof DustMaterial && mat.hasFlag(GENERATE_ORE)),
    crushed("Crushed Ores", -1, null, MaterialIconType.crushed, ENABLE_UNIFICATION | DISALLOW_RECYCLING, (mat) -> mat instanceof DustMaterial && mat.hasFlag(GENERATE_ORE)),

    dustImpure("Impure Dusts", M, null, MaterialIconType.dustImpure, ENABLE_UNIFICATION | DISALLOW_RECYCLING, mat -> mat instanceof DustMaterial && mat.hasFlag(GENERATE_ORE)), // Dust with impurities. 1 Unit of Main Material and 1/9 - 1/4 Unit of secondary Material
    dustPure("Purified Dusts", M, null, MaterialIconType.dustPure, ENABLE_UNIFICATION | DISALLOW_RECYCLING, mat -> mat instanceof DustMaterial && mat.hasFlag(GENERATE_ORE)),


    ingotHot("Hot Ingots", M, null, MaterialIconType.ingotHot, ENABLE_UNIFICATION | DISALLOW_RECYCLING, mat -> (mat instanceof IngotMaterial) && ((IngotMaterial) mat).blastFurnaceTemperature > 1750), // A hot Ingot, which has to be cooled down by a Vacuum Freezer.
    ingot("Ingots", M, null, MaterialIconType.ingot, ENABLE_UNIFICATION | DISALLOW_RECYCLING, mat -> mat instanceof IngotMaterial), // A regular Ingot. Introduced by Eloraam
    nugget("Nuggets", M / 9, null, MaterialIconType.nugget, ENABLE_UNIFICATION | DISALLOW_RECYCLING, mat -> mat instanceof IngotMaterial), // A Nugget. Introduced by Eloraam

    gem("Gemstones", M, null, MaterialIconType.gem, ENABLE_UNIFICATION, mat -> mat instanceof GemMaterial), // A regular Gem worth one Dust. Introduced by Eloraam
    gemChipped("Chipped Gemstones", M / 4, null, MaterialIconType.gemChipped, ENABLE_UNIFICATION, mat -> mat instanceof GemMaterial), // A regular Gem worth one small Dust. Introduced by TerraFirmaCraft
    gemFlawed("Flawed Gemstones", M / 2, null, MaterialIconType.gemFlawed, ENABLE_UNIFICATION, mat -> mat instanceof GemMaterial), // A regular Gem worth two small Dusts. Introduced by TerraFirmaCraft
    gemFlawless("Flawless Gemstones", M * 2, null, MaterialIconType.gemFlawless, ENABLE_UNIFICATION, mat -> mat instanceof GemMaterial), // A regular Gem worth two Dusts. Introduced by TerraFirmaCraft
    gemExquisite("Exquisite Gemstones", M * 4, null, MaterialIconType.gemExquisite, ENABLE_UNIFICATION, mat -> mat instanceof GemMaterial), // A regular Gem worth four Dusts. Introduced by TerraFirmaCraft
*/
}
