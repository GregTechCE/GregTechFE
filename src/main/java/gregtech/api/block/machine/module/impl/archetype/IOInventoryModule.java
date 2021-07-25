package gregtech.api.block.machine.module.impl.archetype;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.filter.ItemFilter;
import org.jetbrains.annotations.NotNull;

public interface IOInventoryModule {

    void setImportItemFilter(ItemFilter filter);

    void setImportFluidFilter(FluidFilter filter);

    void addInputChangeListener(InventoryChangeListener listener);

    void addOutputChangeListener(InventoryChangeListener listener);

    @NotNull
    FixedItemInv getItemImportInventory();

    @NotNull
    FixedItemInv getItemExportInventory();

    @NotNull
    FixedFluidInv getFluidImportInventory();

    @NotNull
    FixedFluidInv getFluidExportInventory();

    @FunctionalInterface
    interface InventoryChangeListener {
        void onChanged();
    }
}
