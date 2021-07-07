package gregtech.api.items.material;

import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import com.google.common.base.Preconditions;
import gregtech.api.fluids.MaterialFluidRegistry;
import gregtech.api.unification.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;

public class MaterialItemFormBucket extends MaterialItemForm {

    public MaterialItemFormBucket(Settings settings) {
        super(settings);
    }

    protected Fluid getMaterialFluid(Material material) {
        FluidKey materialFluid = MaterialFluidRegistry.INSTANCE.getMaterialFluid(material);
        Fluid fluid = materialFluid.getRawFluid();
        Preconditions.checkNotNull(fluid, "Fluid is not set for material %s", material);
        return fluid;
    }

    @Override
    public Item createItem(Material material) {
        Fluid materialFluid = getMaterialFluid(material);
        return new BucketItem(materialFluid, createItemSettings(material));
    }
}
