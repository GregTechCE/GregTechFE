package gregtech.api.capability.internal;

import alexiil.mc.lib.attributes.CombinableAttribute;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.ItemAttributes;
import com.google.common.base.MoreObjects;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.block.EnergyContainer;

import java.util.List;

public class MultiblockAbility<T> {

    public static final MultiblockAbility<FixedItemInv> EXPORT_ITEMS = new MultiblockAbility<>("export_items", ItemAttributes.FIXED_INV);
    public static final MultiblockAbility<FixedItemInv> IMPORT_ITEMS = new MultiblockAbility<>("import_items", ItemAttributes.FIXED_INV);

    public static final MultiblockAbility<FixedFluidInv> EXPORT_FLUIDS = new MultiblockAbility<>("export_fluids", FluidAttributes.FIXED_INV);
    public static final MultiblockAbility<FixedFluidInv> IMPORT_FLUIDS = new MultiblockAbility<>("import_fluids", FluidAttributes.FIXED_INV);

    public static final MultiblockAbility<EnergyContainer> INPUT_ENERGY = new MultiblockAbility<>("input_energy", GTAttributes.ENERGY_CONTAINER);
    public static final MultiblockAbility<EnergyContainer> OUTPUT_ENERGY = new MultiblockAbility<>("output_energy", GTAttributes.ENERGY_CONTAINER);

    private final String name;
    private final CombinableAttribute<T> attribute;

    public MultiblockAbility(String name, CombinableAttribute<T> attribute) {
        this.name = name;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public T cast(Object object) {
        return this.attribute.cast(object);
    }

    public T combine(List<T> attributes) {
        return this.attribute.combine(attributes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("attribute", attribute)
                .toString();
    }
}
