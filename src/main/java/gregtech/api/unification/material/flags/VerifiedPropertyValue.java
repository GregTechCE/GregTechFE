package gregtech.api.unification.material.flags;

import gregtech.api.unification.material.Material;

public interface VerifiedPropertyValue {

    boolean verifyValue(Material ownedMaterial, MaterialProperty<?> parentProperty);
}
