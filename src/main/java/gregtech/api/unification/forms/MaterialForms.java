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

    public static final MaterialForm INGOTS;
    public static final MaterialForm NUGGETS;

    public static final MaterialForm PLATES;
    public static final MaterialForm DENSE_PLATES;

    public static final MaterialForm RODS;
    public static final MaterialForm LONG_RODS;

    public static final MaterialForm GEARS;
    public static final MaterialForm SMALL_GEARS;


    public static final MaterialForm GEM;
    public static final MaterialForm POLYMER;

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
    }


}
