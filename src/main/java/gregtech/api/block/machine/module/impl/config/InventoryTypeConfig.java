package gregtech.api.block.machine.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.block.machine.module.MachineModuleConfig;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.archetype.IOInventoryModule;

public class InventoryTypeConfig implements MachineModuleConfig {

    private final MachineModuleType<?, ? extends IOInventoryModule> moduleType;

    protected InventoryTypeConfig(MachineModuleType<?, ? extends IOInventoryModule> moduleType) {
        Preconditions.checkNotNull(moduleType, "moduleType");
        this.moduleType = moduleType;
    }

    public MachineModuleType<?, ? extends IOInventoryModule> getInventoryModuleType() {
        return moduleType;
    }

    public static InventoryTypeConfig ofInventory(MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType) {
        return new InventoryTypeConfig(inventoryModuleType);
    }
}
