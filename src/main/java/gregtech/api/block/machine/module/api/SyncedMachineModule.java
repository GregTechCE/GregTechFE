package gregtech.api.block.machine.module.api;

import gregtech.api.block.machine.module.MachineModule;
import net.minecraft.nbt.NbtElement;

public interface SyncedMachineModule {
    
    NbtElement writeSyncData();

    void readSyncData(NbtElement nbt);

    default void sync() {
        MachineModule<?> machineModule = (MachineModule<?>) this;
        machineModule.getMachine().sync();
    }

    default void markDirtyAndSync() {
        MachineModule<?> machineModule = (MachineModule<?>) this;
        machineModule.getMachine().markDirty();
        machineModule.getMachine().sync();
    }
}
