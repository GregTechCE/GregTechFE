package gregtech.api.block.machine.module.impl;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.LimitedFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.filter.ConstantFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.SimpleLimitedFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.LimitedFixedItemInv;
import alexiil.mc.lib.attributes.item.filter.ConstantItemFilter;
import alexiil.mc.lib.attributes.item.filter.ItemFilter;
import alexiil.mc.lib.attributes.item.impl.SimpleLimitedFixedItemInv;
import alexiil.mc.lib.attributes.misc.NullVariant;
import alexiil.mc.lib.attributes.misc.Saveable;
import com.google.common.base.Preconditions;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.api.AttributeProviderModule;
import gregtech.api.block.machine.module.api.InventoryClearNotifyModule;
import gregtech.api.block.machine.module.api.PersistentMachineModule;
import gregtech.api.block.machine.module.impl.archetype.IOInventoryModule;
import gregtech.api.block.machine.module.impl.config.SimpleInventoryConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleInventoryModule extends MachineModule<SimpleInventoryConfig> implements IOInventoryModule, PersistentMachineModule, AttributeProviderModule, InventoryClearNotifyModule {

    private final FixedItemInv itemImportInventory;
    private final FixedItemInv itemExportInventory;
    private final FixedFluidInv fluidImportInventory;
    private final FixedFluidInv fluidExportInventory;

    private ItemFilter importItemFilter = ConstantItemFilter.ANYTHING;
    private FluidFilter importFluidFilter = ConstantFluidFilter.ANYTHING;
    private final List<Object> limitedInventoryViews = new ArrayList<>();
    private final List<InventoryChangeListener> inputListeners = new ArrayList<>();
    private final List<InventoryChangeListener> outputListeners = new ArrayList<>();

    public SimpleInventoryModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, SimpleInventoryConfig config) {
        super(machine, type, config);

        this.itemImportInventory = config.createItemImportInventory();
        this.itemExportInventory = config.createItemExportInventory();
        this.fluidImportInventory = config.createFluidImportInventory();
        this.fluidExportInventory = config.createFluidExportInventory();
        configureLimitedViews();
        attachListeners();
    }

    private void attachListeners() {
        ListenerRemovalToken removalToken = () -> {};
        this.itemImportInventory.addListener(inv -> fireInputChangeListeners(), removalToken);
        this.fluidImportInventory.addListener((inv, tank, previous, current) -> fireInputChangeListeners(), removalToken);

        this.itemExportInventory.addListener(inv -> fireOutputChangeListeners(), removalToken);
        this.fluidExportInventory.addListener((inv, tank, previous, current) -> fireOutputChangeListeners(), removalToken);
    }

    private void fireInputChangeListeners() {
        for (InventoryChangeListener listener : this.inputListeners) {
            listener.onChanged();
        }
    }

    private void fireOutputChangeListeners() {
        for (InventoryChangeListener listener : this.outputListeners) {
            listener.onChanged();
        }
    }

    @SuppressWarnings("Convert2MethodRef")
    private void configureLimitedViews() {
        createLimitedView(itemImportInventory, rule -> rule.disallowExtraction().filterInserts(this::canImportItemStack));
        createLimitedView(itemExportInventory, rule -> rule.disallowInsertion());

        createLimitedView(fluidImportInventory, rule -> rule.disallowExtraction().filterInserts(this::canImportFluidKey));
        createLimitedView(fluidExportInventory, rule -> rule.disallowInsertion());
    }

    private void createLimitedView(FixedItemInv original, Consumer<LimitedFixedItemInv.ItemSlotLimitRule> configure) {
        if (!(original instanceof NullVariant)) {
            SimpleLimitedFixedItemInv limitedView = new SimpleLimitedFixedItemInv(original);
            configure.accept(limitedView.getAllRule());
            limitedView.markFinal();
            this.limitedInventoryViews.add(limitedView);
        }
    }

    private void createLimitedView(FixedFluidInv original, Consumer<LimitedFixedFluidInv.FluidTankLimitRule> configure) {
        if (!(original instanceof NullVariant)) {
            SimpleLimitedFixedFluidInv limitedView = new SimpleLimitedFixedFluidInv(original);
            configure.accept(limitedView.getAllRule());
            limitedView.markFinal();
            this.limitedInventoryViews.add(limitedView);
        }
    }

    @Override
    public void clearInventory(List<ItemStack> droppedItems, Simulation simulation) {
        InventoryClearNotifyModule.clearInventory(this.itemImportInventory, droppedItems, simulation);
        InventoryClearNotifyModule.clearInventory(this.itemExportInventory, droppedItems, simulation);
    }

    @Override
    public void addAllAttributes(AttributeList<?> attributeList) {
        this.limitedInventoryViews.forEach(attributeList::offer);
    }

    protected boolean canImportItemStack(ItemStack itemStack) {
        return this.importItemFilter.matches(itemStack);
    }

    protected boolean canImportFluidKey(FluidKey fluidKey) {
        return this.importFluidFilter.matches(fluidKey);
    }

    @Override
    public void setImportItemFilter(ItemFilter filter) {
        Preconditions.checkNotNull(filter);
        this.importItemFilter = filter;
    }

    @Override
    public void setImportFluidFilter(FluidFilter filter) {
        Preconditions.checkNotNull(filter);
        this.importFluidFilter = filter;
    }

    @Override
    public void addInputChangeListener(InventoryChangeListener listener) {
        Preconditions.checkNotNull(listener);
        this.inputListeners.add(listener);
    }

    @Override
    public void addOutputChangeListener(InventoryChangeListener listener) {
        Preconditions.checkNotNull(listener);
        this.outputListeners.add(listener);
    }

    @Override
    public @NotNull FixedItemInv getItemImportInventory() {
        return itemImportInventory;
    }

    @Override
    public @NotNull FixedItemInv getItemExportInventory() {
        return itemExportInventory;
    }

    @Override
    public @NotNull FixedFluidInv getFluidImportInventory() {
        return fluidImportInventory;
    }

    @Override
    public @NotNull FixedFluidInv getFluidExportInventory() {
        return fluidExportInventory;
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        if (itemImportInventory instanceof Saveable inventory) {
            nbt.put("ItemImportInventory", inventory.toTag());
        }
        if (itemExportInventory instanceof Saveable inventory) {
            nbt.put("ItemExportInventory", inventory.toTag());
        }
        if (fluidImportInventory instanceof Saveable inventory) {
            nbt.put("FluidImportInventory", inventory.toTag());
        }
        if (fluidExportInventory instanceof Saveable inventory) {
            nbt.put("FluidExportInventory", inventory.toTag());
        }
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        if (itemImportInventory instanceof Saveable inventory) {
            if (nbt.contains("ItemImportInventory", NbtElement.COMPOUND_TYPE)) {
                inventory.fromTag(nbt.getCompound("ItemImportInventory"));
            }
        }
        if (itemExportInventory instanceof Saveable inventory) {
            if (nbt.contains("ItemExportInventory", NbtElement.COMPOUND_TYPE)) {
                inventory.fromTag(nbt.getCompound("ItemExportInventory"));
            }
        }
        if (fluidImportInventory instanceof Saveable inventory) {
            if (nbt.contains("FluidImportInventory", NbtElement.COMPOUND_TYPE)) {
                inventory.fromTag(nbt.getCompound("FluidImportInventory"));
            }
        }
        if (fluidExportInventory instanceof Saveable inventory) {
            if (nbt.contains("FluidExportInventory", NbtElement.COMPOUND_TYPE)) {
                inventory.fromTag(nbt.getCompound("FluidExportInventory"));
            }
        }
    }

}
