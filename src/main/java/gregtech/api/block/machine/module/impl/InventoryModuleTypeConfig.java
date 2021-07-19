package gregtech.api.block.machine.module.impl;

import com.google.common.base.Preconditions;
import gregtech.api.block.machine.module.MachineModuleConfig;
import gregtech.api.block.machine.module.MachineModuleType;

public class InventoryModuleTypeConfig implements MachineModuleConfig {

    private final MachineModuleType<?, ? extends IOInventoryModule> moduleType;

    protected InventoryModuleTypeConfig(MachineModuleType<?, ? extends IOInventoryModule> moduleType) {
        Preconditions.checkNotNull(moduleType, "moduleType");
        this.moduleType = moduleType;
    }

    public MachineModuleType<?, ? extends IOInventoryModule> getInventoryModuleType() {
        return moduleType;
    }

    public static InventoryModuleTypeConfig ofInventory(MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType) {
        return new InventoryModuleTypeConfig(inventoryModuleType);
    }
}
