package gregtech.api.block.machine.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.block.machine.module.MachineModuleConfig;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.archetype.EnergyContainerModule;

public class EnergyContainerTypeConfig implements MachineModuleConfig {

    private final MachineModuleType<?, ? extends EnergyContainerModule> moduleType;

    protected EnergyContainerTypeConfig(MachineModuleType<?, ? extends EnergyContainerModule> moduleType) {
        Preconditions.checkNotNull(moduleType, "moduleType");
        this.moduleType = moduleType;
    }

    public MachineModuleType<?, ? extends EnergyContainerModule> getEnergyContainerModuleType() {
        return moduleType;
    }

    public static EnergyContainerTypeConfig ofEnergyContainer(MachineModuleType<?, ? extends EnergyContainerModule> moduleType) {
        return new EnergyContainerTypeConfig(moduleType);
    }
}
