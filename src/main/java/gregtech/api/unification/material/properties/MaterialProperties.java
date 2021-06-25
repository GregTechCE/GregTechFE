package gregtech.api.unification.material.properties;

import gregtech.api.unification.material.MaterialIconSet;

public class MaterialProperties {
    public static final MaterialProperty<?> COLOR = new MaterialProperty.Builder<>("Color", Integer.class).build();
    public static final MaterialProperty<?> ICON_SET = new MaterialProperty.Builder<>("IconSet", MaterialIconSet.class).build();

    private  MaterialProperties(){

    }
}
