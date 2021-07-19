package gregtech.api.block.machine.module.api;

import gregtech.api.block.machine.MachineTickType;

public interface TickableMachineModule {

    void tick(MachineTickType tickType);
}
