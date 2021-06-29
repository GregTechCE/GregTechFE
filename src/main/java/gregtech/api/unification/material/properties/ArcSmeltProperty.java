package gregtech.api.unification.material.properties;

import com.google.common.base.Preconditions;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.material.Material;

public class ArcSmeltProperty {

    private final Material arcSmeltInto;

    private ArcSmeltProperty(Material arcSmeltInto) {
        this.arcSmeltInto = arcSmeltInto;
        validateProperty();
    }

    public static ArcSmeltProperty arcSmeltInto(Material arcSmeltInto) {
        return new ArcSmeltProperty(arcSmeltInto);
    }

    public Material getArcSmeltInto() {
        return arcSmeltInto;
    }

    private void validateProperty() {
        Preconditions.checkNotNull(arcSmeltInto, "material %s arcSmeltInto is null");
        Preconditions.checkState(arcSmeltInto.hasFlag(MaterialFlags.SOLID_FORM),
                "arcSmeltInto %s has no solid form", arcSmeltInto);
        Preconditions.checkState(arcSmeltInto.queryPropertyChecked(MaterialFlags.SOLID_FORM) == SolidForm.METAL,
                "arcSmeltInto %s is not a metal", arcSmeltInto);
    }
}
