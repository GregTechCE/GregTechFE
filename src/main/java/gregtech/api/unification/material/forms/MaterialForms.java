package gregtech.api.unification.material.forms;

import static gregtech.api.GTValues.M;

public class MaterialForms {
    public static final MaterialForm INGOT = new MaterialForm.Builder("INGOT").build();
    public static final MaterialForm PLATE = new MaterialForm.Builder("PLATE").build();
    public static final MaterialForm NUGGET = new MaterialForm.Builder("NUGGET").materialAmount(M / 9).build();

    private MaterialForms() {

    }
}
