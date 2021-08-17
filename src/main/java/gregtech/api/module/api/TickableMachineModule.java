package gregtech.api.module.api;

import gregtech.api.block.machine.MachineTickType;

public interface TickableMachineModule {

    void tick(MachineTickType tickType);
}
