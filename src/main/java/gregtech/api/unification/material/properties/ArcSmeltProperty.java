package gregtech.api.unification.material.properties;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.MaterialFlags;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ArcSmeltProperty {

    private final Supplier<Material> arcSmeltInto;

    private ArcSmeltProperty(Supplier<Material> arcSmeltInto) {
        this.arcSmeltInto = arcSmeltInto;
    }

    public static ArcSmeltProperty arcSmeltInto(Supplier<Material> arcSmeltInto) {
        Preconditions.checkNotNull(arcSmeltInto);
        return new ArcSmeltProperty(Suppliers.memoize(arcSmeltInto::get));
    }

    @Nullable
    public Material getArcSmeltInto() {
        if (this.arcSmeltInto != null) {
            return Preconditions.checkNotNull(arcSmeltInto.get());
        }
        return null;
    }

    private void validateProperty() {
        //TODO: validation has to be called after all materials are initialized

        Material material = arcSmeltInto.get();

        Preconditions.checkNotNull(material, "material %s arcSmeltInto is null");
        Preconditions.checkState(material.hasFlag(MaterialFlags.SOLID_FORM),
                "arcSmeltInto %s has no solid form", arcSmeltInto);
        Preconditions.checkState(material.queryPropertyChecked(MaterialFlags.SOLID_FORM) == SolidForm.METAL,
                "arcSmeltInto %s is not a metal", arcSmeltInto);
    }
}
