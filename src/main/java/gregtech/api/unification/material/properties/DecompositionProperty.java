package gregtech.api.unification.material.properties;

import com.google.common.base.Preconditions;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.material.Material;
import org.jetbrains.annotations.Nullable;

public class DecompositionProperty {

    private final DecompositionMode decompositionMode;
    private final Material requiredFluid;

    private DecompositionProperty(DecompositionMode decompositionMode, Material requiredFluid) {
        Preconditions.checkNotNull(decompositionMode);
        this.decompositionMode = decompositionMode;
        this.requiredFluid = requiredFluid;
        validate();
    }

    private void validate() {
        if (requiredFluid != null) {
            boolean hasFlag = requiredFluid.hasFlag(MaterialFlags.FLUID_PROPERTIES);
            String message = "Material %s, used as requiredFluid, must have Fluid Properties";
            Preconditions.checkArgument(hasFlag, message, this.requiredFluid);
        }
    }

    public DecompositionMode getDecompositionMode() {
        return decompositionMode;
    }

    @Nullable
    public Material getRequiredFluid() {
        return requiredFluid;
    }

    public enum DecompositionMode {
        ELECTROLYSIS,
        CENTRIFUGE
    }
}
