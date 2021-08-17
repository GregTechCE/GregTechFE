package gregtech.api.module;

import gregtech.api.block.machine.MachineBlockEntity;

public class MachineModule<C extends MachineModuleConfig> {

    protected final MachineBlockEntity machine;
    protected final MachineModuleType<?, ?> type;
    protected final C config;

    public MachineModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, C config) {
        this.machine = machine;
        this.type = type;
        this.config = config;
    }

    public void onModulesReady() {
    }

    public MachineBlockEntity getMachine() {
        return machine;
    }

    public MachineModuleType<?, ?> getType() {
        return type;
    }

    public C getConfig() {
        return config;
    }
}
