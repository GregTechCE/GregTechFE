package gregtech.api.block.machine.module.impl;

import alexiil.mc.lib.attributes.Simulation;
import com.google.common.base.Preconditions;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.api.*;
import gregtech.api.block.machine.module.impl.config.OrientationConfig;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

public class OrientationModule extends MachineModule<OrientationConfig> implements SyncedMachineModule, PersistentMachineModule, ModelStateAwareModule, OrientableMachineModule {

    private Direction orientation;

    public OrientationModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, OrientationConfig config) {
        super(machine, type, config);
        this.orientation = Direction.NORTH;
    }

    @Override
    public Direction getOrientation() {
        return orientation;
    }

    public void setOrientation(Direction orientation) {
        Preconditions.checkNotNull(orientation, "orientation");
        this.orientation = orientation;
        markDirtyAndSync();
    }

    protected boolean supportsOrientation(Direction orientation) {
        return orientation.getAxis().isHorizontal() || this.config.supportsVerticalOrientation();
    }

    @Override
    public OrientationKind getOrientationKind() {
        return StandardOrientationKind.FRONT_FACING;
    }

    @Override
    public ActionResult attemptSetOrientation(Direction newOrientation, LivingEntity player, Simulation simulation) {
        if (!player.isSneaking()) {
            if (supportsOrientation(newOrientation) && newOrientation != getOrientation()) {
                if (simulation.isAction()) {
                    setOrientation(newOrientation);
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    @Override
    public void setupModelState(ModelState.Builder<?> builder) {
        builder.with(ModelStateProperties.ORIENTATION, this.orientation);
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        nbt.putInt("Orientation", this.orientation.getId());
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        if (nbt.contains("Orientation", NbtElement.NUMBER_TYPE)) {
            this.orientation = Direction.byId(nbt.getInt("Orientation"));
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
