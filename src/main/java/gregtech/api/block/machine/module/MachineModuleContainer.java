package gregtech.api.block.machine.module;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.block.machine.module.api.*;
import gregtech.api.render.model.state.ModelState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineModuleContainer {

    private final MachineBlockEntity machine;
    private final Map<MachineModuleType<?, ?>, MachineModule<?>> modules = new HashMap<>();

    public MachineModuleContainer(MachineBlockEntity machine, Collection<ConfiguredModule<?>> modules) {
        this.machine = machine;
        for (ConfiguredModule<?> machineModule : modules) {
            this.modules.put(machineModule.getModuleType(), machineModule.createModule(machine));
        }
    }

    public MachineBlockEntity getMachine() {
        return machine;
    }

    public <T extends MachineModule<?>> T getModule(MachineModuleType<?, T> type) {
        MachineModule<?> machineModule = this.modules.get(type);
        return type.cast(machineModule);
    }

    public boolean attemptSetOrientation(Direction newOrientation, @Nullable LivingEntity player, Simulation simulation) {
        boolean orientationSet = false;

        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof OrientableMachineModule orientableMachineModule) {
                if (orientableMachineModule.supportsOrientation(newOrientation)) {

                    orientationSet = orientableMachineModule.attemptSetOrientation(newOrientation, player, simulation);
                    if (orientationSet) { break; }
                }
            }
        }

        if (orientationSet && simulation.isAction()) {
            for (MachineModule<?> machineModule : this.modules.values()) {
                if (machineModule instanceof OrientableMachineModule orientableMachineModule) {
                    orientableMachineModule.onOrientationSet(newOrientation);
                }
            }
        }
        return orientationSet;
    }

    public void clearInventory(List<ItemStack> droppedStacks, Simulation simulation) {
        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof InventoryClearNotifyModule notifyModule) {
                notifyModule.clearInventory(droppedStacks, simulation);
            }
        }
    }

    public void setupModelState(ModelState.Builder<?> builder) {
        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof ModelStateAwareModule modelStateAwareModule) {
                modelStateAwareModule.setupModelState(builder);
            }
        }
    }

    public void addAllAttributes(AttributeList<?> attributeList) {
        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof AttributeProviderModule attributeProviderModule) {
                attributeProviderModule.addAllAttributes(attributeList);
            }
        }
    }

    public void tickModules(MachineTickType tickType) {
        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof TickableMachineModule tickableMachineModule) {
                tickableMachineModule.tick(tickType);
            }
        }
    }

    public void writeClientData(NbtCompound nbt) {
        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof SyncedMachineModule syncedMachineModule) {

                int moduleId = MachineModuleType.REGISTRY.getRawId(machineModule.getType());
                NbtElement syncData = syncedMachineModule.writeSyncData();
                nbt.put(Integer.toString(moduleId), syncData);
            }
        }
    }

    public void readClientData(NbtCompound nbt) {
        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof SyncedMachineModule syncedMachineModule) {
                int moduleId = MachineModuleType.REGISTRY.getRawId(machineModule.getType());
                String moduleIdString = Integer.toString(moduleId);

                if (nbt.contains(moduleIdString)) {
                    NbtElement syncData = nbt.get(moduleIdString);
                    syncedMachineModule.readSyncData(syncData);
                }
            }
        }
    }

    public void writePersistenceData(NbtCompound nbt) {
        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof PersistentMachineModule persistentModule) {

                Identifier moduleId = machineModule.getType().getId();
                NbtCompound moduleNbt = new NbtCompound();

                persistentModule.writePersistenceData(moduleNbt);
                nbt.put(moduleId.toString(), moduleNbt);
            }
        }
    }

    public void readPersistenceData(NbtCompound nbt) {
        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof PersistentMachineModule persistentModule) {

                String moduleId = machineModule.getType().getId().toString();
                if (nbt.contains(moduleId, NbtElement.COMPOUND_TYPE)) {

                    NbtCompound moduleNbt = nbt.getCompound(moduleId);
                    persistentModule.readPersistenceData(moduleNbt);
                }
            }
        }
    }
}
