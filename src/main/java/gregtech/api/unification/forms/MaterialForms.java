package gregtech.api.unification.forms;

import gregtech.api.GTValues;
import gregtech.api.unification.util.MaterialAmount;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialForms {

    public static final MaterialForm ORES;
    public static final MaterialForm BLOCKS;

    public static final MaterialForm DUSTS;
    public static final MaterialForm SMALL_DUSTS;
    public static final MaterialForm TINY_DUSTS;

    public static final MaterialForm GEMS;
    public static final MaterialForm INGOTS;
    public static final MaterialForm NUGGETS;

    public static final MaterialForm PLATES;
    public static final MaterialForm DENSE_PLATES;

    public static final MaterialForm RODS;
    public static final MaterialForm LONG_RODS;

    public static final MaterialForm GEARS;
    public static final MaterialForm SMALL_GEARS;

    public static final MaterialForm SPRINGS;
    public static final MaterialForm SMALL_SPRINGS;

    public static final MaterialForm FINE_WIRES;
    public static final MaterialForm FOILS;
    public static final MaterialForm BOLTS;
    public static final MaterialForm SCREWS;
    public static final MaterialForm RINGS;
    public static final MaterialForm ROTORS;
    public static final MaterialForm LENSES;

    public static final MaterialForm CHIPPED_GEMS;
    public static final MaterialForm FLAWED_GEMS;
    public static final MaterialForm FLAWLESS_GEMS;
    public static final MaterialForm EXQUISITE_GEMS;

    private static MaterialForm register(String name, MaterialForm materialForm) {
        return Registry.register(MaterialForm.REGISTRY, new Identifier(GTValues.MODID, name), materialForm);
    }

    static {
        ORES = register("ores", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_ores")));

        BLOCKS = register("blocks", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_blocks")));

        DUSTS = register("dusts", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_dusts").materialAmount(MaterialAmount.DUST)));

        SMALL_DUSTS = register("small_dusts", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_small_dusts").materialAmount(MaterialAmount.SMALL_DUST)));

        TINY_DUSTS = register("tiny_dusts", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_tiny_dusts").materialAmount(MaterialAmount.TINY_DUST)));

        GEMS = register("gems", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_gems").materialAmount(MaterialAmount.GEM)));

        INGOTS = register("ingots", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_ingots").materialAmount(MaterialAmount.INGOT)));

        NUGGETS = register("nuggets", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_nuggets").materialAmount(MaterialAmount.NUGGET)));

        PLATES = register("plates", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_plates").materialAmount(MaterialAmount.PLATE)));

        DENSE_PLATES = register("dense_plates", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_dense_plates").materialAmount(MaterialAmount.DENSE_PLATE)));

        RODS = register("rods", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_rods").materialAmount(MaterialAmount.ROD)));

        LONG_RODS = register("long_rods", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_long_rods").materialAmount(MaterialAmount.LONG_ROD)));

        GEARS = register("gears", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_gears").materialAmount(MaterialAmount.GEAR)));

        SMALL_GEARS = register("small_gears", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_small_gears").materialAmount(MaterialAmount.SMALL_GEAR)));

        SPRINGS = register("springs", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_springs").materialAmount(MaterialAmount.SPRING)));

        SMALL_SPRINGS = register("small_springs", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_small_springs").materialAmount(MaterialAmount.SMALL_SPRING)));

        FINE_WIRES = register("fine_wires", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_fine_wires").materialAmount(MaterialAmount.FINE_WIRE)));

        FOILS = register("foils", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_foils").materialAmount(MaterialAmount.FOIL)));

        BOLTS = register("bolts", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_bolts").materialAmount(MaterialAmount.BOLT)));

        SCREWS = register("screws", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_screws").materialAmount(MaterialAmount.SCREW)));

        RINGS = register("rings", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_rings").materialAmount(MaterialAmount.RING)));

        ROTORS = register("rotors", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_rotors").materialAmount(MaterialAmount.ROTOR)));

        LENSES = register("lenses", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_lenses").materialAmount(MaterialAmount.LENS)));

        CHIPPED_GEMS = register("chipped_gems", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_chipped_gems").materialAmount(MaterialAmount.CHIPPED_GEM)));

        FLAWED_GEMS = register("flawed_gems", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_flawed_gems").materialAmount(MaterialAmount.FLAWED_GEM)));

        FLAWLESS_GEMS = register("flawless_gems", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_flawless_gems").materialAmount(MaterialAmount.FLAWLESS_GEM)));

        EXQUISITE_GEMS = register("exquisite_gems", new MaterialForm(new MaterialForm.Settings()
                .tagNameTemplate("{material}_exquisite_gems").materialAmount(MaterialAmount.EXQUISITE_GEM)));
    }

}
