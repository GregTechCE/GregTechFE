package gregtech.api.block.ore;

import com.google.common.base.MoreObjects;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.ore.OreBlockType;
import gregtech.api.unification.ore.OreVariant;
import gregtech.api.unification.util.MaterialIconSet;

import java.util.Objects;

public class VisualOreBlockType {

    private final OreVariant oreVariant;
    private final MaterialIconSet materialIconSet;

    public VisualOreBlockType(OreVariant oreVariant, MaterialIconSet materialIconSet) {
        this.oreVariant = oreVariant;
        this.materialIconSet = materialIconSet;
    }

    public VisualOreBlockType(OreBlockType oreBlockType) {
        this(oreBlockType.getVariant(), oreBlockType.getMaterial().queryPropertyChecked(MaterialFlags.ICON_SET));
    }

    public OreVariant getOreVariant() {
        return oreVariant;
    }

    public MaterialIconSet getMaterialIconSet() {
        return materialIconSet;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("oreVariant", oreVariant)
                .add("materialIconSet", materialIconSet)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisualOreBlockType that = (VisualOreBlockType) o;
        return oreVariant.equals(that.oreVariant) && materialIconSet.equals(that.materialIconSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oreVariant, materialIconSet);
    }
}
