package gregtech.api.unification.material.flags;

import gregtech.api.unification.material.Material;

public interface VerifiedPropertyValue {

    void verifyValue(Material ownedMaterial, MaterialProperty<?> parentProperty) throws RuntimeException;
}
