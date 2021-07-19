package gregtech.api.block.machine.module.impl;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.api.ModelStateAwareModule;
import gregtech.api.block.machine.module.api.OrientableMachineModule;
import gregtech.api.block.machine.module.api.PersistentMachineModule;
import gregtech.api.block.machine.module.api.SyncedMachineModule;
import gregtech.api.block.machine.module.impl.config.OrientationConfig;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class OrientationModule extends MachineModule<OrientationConfig> implements SyncedMachineModule, PersistentMachineModule, ModelStateAwareModule, OrientableMachineModule {

    private Direction orientation;

    public OrientationModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, OrientationConfig config) {
        super(machine, type, config);
        this.orientation = Direction.NORTH;
    }

    public Direction getOrientation() {
        return orientation;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
        markDirtyAndSync();
    }

    @Override
    public boolean supportsOrientation(Direction orientation) {
        return orientation.getAxis().isHorizontal() || this.config.supportsVerticalOrientation();
    }

    @Override
    public boolean attemptSetOrientation(Direction newOrientation, @Nullable LivingEntity player, Simulation simulation) {
        if (player == null || !player.isSneaking()) {
            if (supportsOrientation(newOrientation) && newOrientation != getOrientation()) {
                if (simulation.isAction()) {
                    setOrientation(newOrientation);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void setupModelState(ModelState.Builder<?> builder) {
        builder.with(ModelStateProperties.ORIENTATION, this.orientation);
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        nbt.putString("Orientation", this.orientation.getName());
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        if (nbt.contains("Orientation", NbtElement.STRING_TYPE)) {
            Direction loadedDirection = Direction.byName(nbt.getString("Orientation"));

            if (loadedDirection != null) {
                this.orientation = loadedDirection;
            }
        }
    }

    @Override
    public NbtElement writeSyncData() {
        return NbtByte.of((byte) this.orientation.getId());
    }

    @Override
    public void readSyncData(NbtElement nbt) {
        this.orientation = Direction.byId(((AbstractNbtNumber) nbt).byteValue());
    }
}
