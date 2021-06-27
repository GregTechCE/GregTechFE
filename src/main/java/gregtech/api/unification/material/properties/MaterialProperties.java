package gregtech.api.unification.material.properties;

import gregtech.api.unification.ChemicalProperty;
import gregtech.api.unification.material.MaterialIconSet;

import static gregtech.api.unification.material.flags.MaterialFlags.FLAMMABLE;

public class MaterialProperties {
    public static final MaterialProperty<Integer> COLOR = new MaterialProperty.Builder<>("Color", Integer.class).build();
    public static final MaterialProperty<MaterialIconSet> ICON_SET = new MaterialProperty.Builder<>("IconSet", MaterialIconSet.class).build();
    public static final MaterialProperty<ChemicalProperty> CHEMICAL_PROPERTY = new MaterialProperty.Builder<>("ChemicalProperty", ChemicalProperty.class).build();

    /**
     * Burn time of this material when used as fuel in furnace smelting
     */
    public static final MaterialProperty<?> BURN_TIME = new MaterialProperty.Builder<>("BURN_TIME", Long.class).requiresFlag(FLAMMABLE).build();

    private  MaterialProperties(){

    }
}
