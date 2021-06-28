package gregtech.api.unification.material.properties;

import gregtech.api.unification.material.forms.MaterialForm;
import gregtech.api.unification.material.forms.MaterialForms;

public enum SolidForm {

    INGOT(MaterialForms.INGOT),
    GEM(MaterialForms.GEM);

    private final MaterialForm solidForm;

    SolidForm(MaterialForm solidForm) {
        this.solidForm = solidForm;
    }

    public MaterialForm getMaterialForm() {
        return this.solidForm;
    }
}
