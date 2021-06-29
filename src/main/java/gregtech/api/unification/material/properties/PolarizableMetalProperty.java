package gregtech.api.unification.material.properties;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.material.flags.MaterialProperty;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.VerifiedPropertyValue;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PolarizableMetalProperty implements VerifiedPropertyValue {

    private final Supplier<Material> polarizedInto;
    private final Supplier<Material> demagnetizedInto;

    public PolarizableMetalProperty(Supplier<Material> polarizedInto, Supplier<Material> demagnetizedInto) {
        this.polarizedInto = polarizedInto;
        this.demagnetizedInto = demagnetizedInto;
    }

    public static PolarizableMetalProperty polarizedInto(Supplier<Material> polarizedInto) {
        Preconditions.checkNotNull(polarizedInto);
        return new PolarizableMetalProperty(Suppliers.memoize(polarizedInto::get), null);
    }

    public static PolarizableMetalProperty demagnetizedInto(Supplier<Material> demagnetizedInto) {
        Preconditions.checkNotNull(demagnetizedInto);
        return new PolarizableMetalProperty(null, Suppliers.memoize(demagnetizedInto::get));
    }

    public boolean isPolarizableMaterial() {
        return this.polarizedInto != null;
    }

    public boolean isMagnetizedMaterial() {
        return this.demagnetizedInto != null;
    }

    @Nullable
    public Material getPolarizedInto() {
        if (this.polarizedInto != null) {
            return Preconditions.checkNotNull(polarizedInto.get());
        }
        return null;
    }

    @Nullable
    public Material getDemagnetizedInto() {
        if (this.demagnetizedInto != null) {
            return Preconditions.checkNotNull(demagnetizedInto.get());
        }
        return null;
    }

    @Override
    public void verifyValue(Material ownedMaterial, MaterialProperty<?> parentProperty) {
        if (polarizedInto != null) {
            validateProperty(ownedMaterial, this.polarizedInto.get());
        }
        if (demagnetizedInto != null) {
            validateProperty(ownedMaterial, this.demagnetizedInto.get());
        }
    }

    private static void validateProperty(Material ownerMaterial, Material material) {
        Preconditions.checkNotNull(material,
                "material %s polarizable material is null", ownerMaterial);
        Preconditions.checkState(material.hasFlag(MaterialFlags.SOLID_FORM),
                "material %s polarizable material %s has no solid form", ownerMaterial, material);
        Preconditions.checkState(material.queryPropertyChecked(MaterialFlags.SOLID_FORM) == SolidForm.METAL,
                "material %s polarizable material %s is not a metal", ownerMaterial, material);
        Preconditions.checkState(material.hasFlag(MaterialFlags.POLARIZABLE_METAL),
                "material %s polarizable material %s is not marked a polarizable metal", ownerMaterial, material);
    }
}
