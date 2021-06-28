package gregtech.api.unification.material.forms;

import gregtech.api.unification.MaterialAmount;

public class MaterialForms {
    public static final MaterialForm INGOT = new MaterialForm.Builder("INGOT").build();
    public static final MaterialForm GEM = new MaterialForm.Builder("GEM").build();
    public static final MaterialForm PLATE = new MaterialForm.Builder("PLATE").build();
    public static final MaterialForm NUGGET = new MaterialForm.Builder("NUGGET").materialAmount(MaterialAmount.NUGGET).build();

    private MaterialForms() {

    }
}
