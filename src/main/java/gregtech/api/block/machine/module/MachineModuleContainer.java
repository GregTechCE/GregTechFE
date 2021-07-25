package gregtech.api.block.machine.module;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import com.google.common.base.Preconditions;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.block.machine.module.api.*;
import gregtech.api.render.model.state.ModelState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MachineModuleContainer {

    private final MachineBlockEntity machine;
    private final Map<MachineModuleType<?, ?>, MachineModule<?>> modules = new HashMap<>();

    public MachineModuleContainer(MachineBlockEntity machine, Collection<ConfiguredModule<?>> modules) {
        this.machine = machine;
        for (ConfiguredModule<?> machineModule : modules) {
            this.modules.put(machineModule.getModuleType(), machineModule.createModule(machine));
        }
        for (MachineModule<?> machineModule : this.modules.values()) {
            machineModule.onModulesReady();
        }
    }

    public MachineBlockEntity getMachine() {
        return machine;
    }

    public <T extends MachineModule<?>> Optional<T> getModule(MachineModuleType<?, T> type) {
        MachineModule<?> machineModule = this.modules.get(type);
        if (machineModule == null) {
            return Optional.empty();
        }
        return Optional.of(type.cast(machineModule));
    }

    public Optional<Direction> getOrientation(OrientationKind orientationKind) {
        Preconditions.checkNotNull(orientationKind, "orientationKind");

        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof OrientableMachineModule orientableMachineModule) {
                if (orientableMachineModule.getOrientationKind() == orientationKind) {
                    return Optional.of(orientableMachineModule.getOrientation());
                }
            }
        }
        return Optional.empty();
    }

    public ActionResult attemptSetOrientation(Direction newOrientation, @NotNull LivingEntity player, Simulation simulation) {
        Preconditions.checkNotNull(newOrientation, "newOrientation");
        Preconditions.checkNotNull(player, "player");

        for (MachineModule<?> machineModule : this.modules.values()) {
            if (machineModule instanceof OrientableMachineModule orientableMachineModule) {
                ActionResult actionResult = orientableMachineModule.attemptSetOrientation(newOrientation, player, simulation);

                if (actionResult != ActionResult.PASS) {
                    return actionResult;
                }
            }
        }
        return ActionResult.PASS;
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
