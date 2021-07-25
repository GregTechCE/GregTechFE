package gregtech.api.block.machine.module.impl;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.impl.DirectFixedItemInv;
import alexiil.mc.lib.attributes.misc.NullVariant;
import alexiil.mc.lib.attributes.misc.Reference;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.api.InventoryClearNotifyModule;
import gregtech.api.block.machine.module.api.PersistentMachineModule;
import gregtech.api.block.machine.module.api.TickableMachineModule;
import gregtech.api.block.machine.module.impl.archetype.EnergyContainerModule;
import gregtech.api.block.machine.module.impl.config.EnergyContainerTypeConfig;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.item.DischargeMode;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.item.TransferLimit;
import gregtech.api.util.ElectricItemUtil;
import gregtech.api.util.ref.FixedInvSlotRef;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.List;

public class ChargerSlotModule extends MachineModule<EnergyContainerTypeConfig> implements PersistentMachineModule, TickableMachineModule, InventoryClearNotifyModule {

    private EnergyContainerModule energyContainerModule;
    private final DirectFixedItemInv chargerInventory;

    public ChargerSlotModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, EnergyContainerTypeConfig config) {
        super(machine, type, config);
        this.chargerInventory = new DirectFixedItemInv(1) {
            @Override
            public boolean isItemValidForSlot(int slot, ItemStack stack) {
                return isItemValidForChargerSlot(stack);
            }
        };
    }

    @Override
    public void onModulesReady() {
        this.energyContainerModule = this.machine.getModuleChecked(this.config.getEnergyContainerModuleType());
    }

    @Override
    public void clearInventory(List<ItemStack> droppedItems, Simulation simulation) {
        InventoryClearNotifyModule.clearInventory(this.chargerInventory, droppedItems, simulation);
    }

    protected boolean isItemValidForChargerSlot(ItemStack stack) {
        EnergyContainer energyContainer = this.energyContainerModule.getEnergyContainer();
        ElectricItem electricItem = GTAttributes.ELECTRIC_ITEM.get(stack);

        return !(electricItem instanceof NullVariant) &&
                electricItem.canProvideChargeExternally() &&
                energyContainer.getVoltageTier().getVoltage() >= electricItem.getVoltageTier().getVoltage();
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        nbt.put("ChargerInventory", this.chargerInventory.toTag());
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        if (nbt.contains("ChargerInventory", NbtElement.COMPOUND_TYPE)) {
            this.chargerInventory.fromTag(nbt.getCompound("ChargerInventory"));
        }
    }

    @Override
    public void tick(MachineTickType tickType) {
        if (tickType.isServer()) {
            processServerTick();
        }
    }

    private void processServerTick() {
        EnergyContainer energyContainer = this.energyContainerModule.getEnergyContainer();
        Reference<ItemStack> itemStackRef = FixedInvSlotRef.of(chargerInventory, 0);

        ElectricItem electricItem = GTAttributes.ELECTRIC_ITEM.get(itemStackRef);
        if (!(electricItem instanceof NullVariant)) {
            double chargePercent = energyContainer.getEnergyStored() / (energyContainer.getEnergyCapacity() * 1.0);

            if (chargePercent <= 0.5) {
                ElectricItemUtil.dischargeElectricItem(electricItem, energyContainer, TransferLimit.RESPECT, DischargeMode.EXTERNAL);
            } else if (chargePercent >= 0.9) {
                ElectricItemUtil.chargeElectricItem(energyContainer, electricItem, TransferLimit.RESPECT);
            }
        }
    }
}
