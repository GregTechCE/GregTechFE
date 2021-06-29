package gregtech.api.unification.forms;

import gregtech.api.unification.material.MaterialAmount;

public class MaterialForms {
    public static final MaterialForm INGOT = new MaterialForm.Builder("INGOT").build();
    public static final MaterialForm GEM = new MaterialForm.Builder("GEM").build();
    public static final MaterialForm PLATE = new MaterialForm.Builder("PLATE").build();
    public static final MaterialForm NUGGET = new MaterialForm.Builder("NUGGET").materialAmount(MaterialAmount.NUGGET).build();
    public static final MaterialForm POLYMER = new MaterialForm.Builder("POLYMER").build();

    private MaterialForms() {

    }
}
