package gregtech.api.module.impl;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.item.ItemAttributes;
import alexiil.mc.lib.attributes.item.ItemExtractable;
import alexiil.mc.lib.attributes.item.ItemInsertable;
import alexiil.mc.lib.attributes.item.ItemInvUtil;
import alexiil.mc.lib.attributes.misc.NullVariant;
import com.google.common.base.Preconditions;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.module.MachineModule;
import gregtech.api.module.MachineModuleType;
import gregtech.api.module.api.*;
import gregtech.api.module.impl.archetype.IOInventoryModule;
import gregtech.api.module.impl.config.InventoryTypeConfig;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

public class AutoOutputModule extends MachineModule<InventoryTypeConfig> implements PersistentMachineModule, SyncedMachineModule, ModelStateAwareModule, OrientableMachineModule, TickableMachineModule {

    private IOInventoryModule inventoryModule;
    private Direction outputOrientation = Direction.SOUTH;
    private boolean autoItemOutputEnabled = false;
    private boolean autoFluidOutputEnabled = false;

    public AutoOutputModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, InventoryTypeConfig config) {
        super(machine, type, config);
    }

    @Override
    public void onModulesReady() {
        this.inventoryModule = getMachine().getModuleChecked(this.getConfig().getInventoryModuleType());
    }

    public Direction getOutputOrientation() {
        return outputOrientation;
    }

    public boolean isAutoItemOutputEnabled() {
        return autoItemOutputEnabled;
    }

    public boolean isAutoFluidOutputEnabled() {
        return autoFluidOutputEnabled;
    }

    public void setOutputOrientation(Direction outputOrientation) {
        Preconditions.checkNotNull(outputOrientation, "outputOrientation");
        this.outputOrientation = outputOrientation;
        markDirtyAndSync();
    }

    public void setAutoItemOutputEnabled(boolean autoItemOutputEnabled) {
        this.autoItemOutputEnabled = autoItemOutputEnabled;
        markDirtyAndSync();
    }

    public void setAutoFluidOutputEnabled(boolean autoFluidOutputEnabled) {
        this.autoFluidOutputEnabled = autoFluidOutputEnabled;
        markDirtyAndSync();
    }

    @Override
    public void tick(MachineTickType tickType) {
        if (tickType.isServer()) {
            if (getMachine().getOffsetWorldTime() % 5 == 0L) {
                pushExportIntoOutputInventory();
            }
        }
    }

    protected void pushExportIntoOutputInventory() {
        ItemExtractable itemExtractable = inventoryModule.getItemExportInventory().getExtractable();
        FluidExtractable fluidExtractable = inventoryModule.getFluidExportInventory().getExtractable();

        if (this.autoItemOutputEnabled && !(itemExtractable instanceof NullVariant)) {
            ItemInsertable insertable = getMachine().getExternalAttribute(ItemAttributes.INSERTABLE, this.outputOrientation);

            if (!(insertable instanceof NullVariant)) {
                ItemInvUtil.moveMultiple(itemExtractable, insertable);
            }
        }

        if (this.autoFluidOutputEnabled && !(fluidExtractable instanceof NullVariant)) {
            FluidInsertable insertable = getMachine().getExternalAttribute(FluidAttributes.INSERTABLE, this.outputOrientation);

            if (!(insertable instanceof NullVariant)) {
                FluidVolumeUtil.move(fluidExtractable, insertable);
            }
        }
    }

    @Override
    public OrientationKind getOrientationKind() {
        return StandardOrientationKind.AUTO_OUTPUT_SIDE;
    }

    @Override
    public Direction getOrientation() {
        return getOutputOrientation();
    }

    @Override
    public ActionResult attemptSetOrientation(Direction newOrientation, LivingEntity player, Simulation simulation) {
        if (player.isSneaking()) {
            if (newOrientation != getOutputOrientation()) {
                if (simulation.isAction()) {
                    setOutputOrientation(newOrientation);
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    @Override
    public void setupModelState(ModelState.Builder<?> builder) {
        builder.with(ModelStateProperties.OUTPUT_DIRECTION, this.outputOrientation);

        builder.with(ModelStateProperties.ITEM_AUTO_OUTPUT_ENABLED, this.autoItemOutputEnabled);
        builder.with(ModelStateProperties.FLUID_AUTO_OUTPUT_ENABLED, this.autoFluidOutputEnabled);
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        nbt.putInt("OutputOrientation", this.outputOrientation.getId());
        nbt.putBoolean("AutoItemOutput", this.autoItemOutputEnabled);
        nbt.putBoolean("AutoFluidOutput", this.autoFluidOutputEnabled);
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        if (nbt.contains("OutputOrientation", NbtElement.NUMBER_TYPE)) {
            this.outputOrientation = Direction.byId(nbt.getInt("OutputOrientation"));
        }
        if (nbt.contains("AutoItemOutput", NbtElement.NUMBER_TYPE)) {
            this.autoItemOutputEnabled = nbt.getBoolean("AutoItemOutput");
        }
        if (nbt.contains("AutoFluidOutput", NbtElement.NUMBER_TYPE)) {
            this.autoFluidOutputEnabled = nbt.getBoolean("AutoFluidOutput");
        }
    }

    @Override
    public NbtElement writeSyncData() {
        byte outputFacing = (byte) (outputOrientation.getId() << 2);
        byte resultFlags = 0;
        if (this.autoItemOutputEnabled) {
            resultFlags += 1;
        }
        if (this.autoFluidOutputEnabled) {
            resultFlags += 2;
        }
        return NbtByte.of((byte) (outputFacing | resultFlags));
    }

    @Override
    public void readSyncData(NbtElement nbt) {
        byte syncFlags = ((AbstractNbtNumber) nbt).byteValue();
        byte outputFacing = (byte) (syncFlags >> 2);
        this.outputOrientation = Direction.byId(outputFacing);
        this.autoItemOutputEnabled = (syncFlags & 1) > 0;
        this.autoFluidOutputEnabled = (syncFlags & 2) > 0;
    }
}
