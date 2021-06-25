package gregtech.api.unification.material;

import gregtech.api.GTValues;
import net.minecraft.util.Identifier;

public class MaterialIconSets {
    public static final MaterialIconSet METALLIC = create("metallic");
    public static final MaterialIconSet DULL = create("dull");
    public static final MaterialIconSet MAGNETIC = create("magnetic");
    public static final MaterialIconSet QUARTZ = create("quartz");
    public static final MaterialIconSet DIAMOND = create("diamond");
    public static final MaterialIconSet EMERALD = create("emerald");
    public static final MaterialIconSet SHINY = create("shiny");
    public static final MaterialIconSet ROUGH = create("rough");
    public static final MaterialIconSet POWDER = create("powder");
    public static final MaterialIconSet FINE = create("fine");
    public static final MaterialIconSet SAND = create("sand");
    public static final MaterialIconSet FLINT = create("flint");
    public static final MaterialIconSet RUBY = create("ruby");
    public static final MaterialIconSet LAPIS = create("lapis");
    public static final MaterialIconSet LIGNITE = create("lignite");
    public static final MaterialIconSet OPAL = create("opal");
    public static final MaterialIconSet GLASS = create("glass");
    public static final MaterialIconSet WOOD = create("wood");
    public static final MaterialIconSet SMOOTH = create("smooth");
    public static final MaterialIconSet GEM_HORIZONTAL = create("gem_horizontal");
    public static final MaterialIconSet GEM_VERTICAL = create("gem_vertical");
    public static final MaterialIconSet PAPER = create("paper");
    public static final MaterialIconSet NETHERSTAR = create("netherstar");

    private static MaterialIconSet create(String name) {
        return new MaterialIconSet(new Identifier(GTValues.MODID, "material_sets/" + name));
    }
}
