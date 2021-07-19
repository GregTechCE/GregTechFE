package gregtech.api.block.machine.module.api;

import gregtech.api.block.machine.module.MachineModule;
import net.minecraft.nbt.NbtCompound;

public interface PersistentMachineModule {

    void writePersistenceData(NbtCompound nbt);

    void readPersistenceData(NbtCompound nbt);

    default void markDirty() {
        MachineModule<?> machineModule = (MachineModule<?>) this;
        machineModule.getMachine().markDirty();
    }
}
