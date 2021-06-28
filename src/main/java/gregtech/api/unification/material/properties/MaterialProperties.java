package gregtech.api.unification.material.properties;

import gregtech.api.GTValues;
import gregtech.api.unification.ChemicalProperty;
import gregtech.api.unification.material.MaterialIconSet;
import gregtech.api.unification.material.MaterialIconSets;
import gregtech.api.unification.material.flags.MaterialFlags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static gregtech.api.unification.material.flags.MaterialFlags.FLAMMABLE;

public class MaterialProperties {

    public static final MaterialProperty<Integer> COLOR;
    public static final MaterialProperty<MaterialIconSet> ICON_SET;
    public static final MaterialProperty<ChemicalComposition> CHEMICAL_COMPOSITION;
    public static final MaterialProperty<Integer> HARVEST_LEVEL;
    public static final MaterialProperty<SolidForm> SOLID_FORM;
    public static final MaterialProperty<ToolProperties> TOOL_PROPERTIES;
    public static final MaterialProperty<Integer> BURN_TIME;

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

        HARVEST_LEVEL = register("harvest_level", new MaterialProperty<>(
                new MaterialProperty.Settings<Integer>()
                    .valueType(Integer.class)
                    .defaultValue(0)
        ));

        SOLID_FORM = register("solid_form", new MaterialProperty<>(
                new MaterialProperty.Settings<SolidForm>()
                    .valueType(SolidForm.class)
        ));

        TOOL_PROPERTIES = register("tool_properties", new MaterialProperty<>(
                new MaterialProperty.Settings<ToolProperties>()
                    .valueType(ToolProperties.class)
                    .requires(SOLID_FORM)
        ));

        BURN_TIME = register("burn_time", new MaterialProperty<>(
                new MaterialProperty.Settings<Integer>()
                    .requires(FLAMMABLE)
        ));
    }

    private static <T> MaterialProperty<T> register(String name, MaterialProperty<T> property) {
        return Registry.register(MaterialProperty.REGISTRY, new Identifier(GTValues.MODID, name), property);
    }
}
