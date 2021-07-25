package gregtech.api.block.machine;

import alexiil.mc.lib.attributes.*;
import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleContainer;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.api.OrientationKind;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MachineBlockEntity extends BlockEntity implements BlockEntityClientSerializable, AttributeProviderBlockEntity, RenderAttachmentBlockEntity {

    private final MachineModuleContainer moduleContainer;
    private final ModelStateManager<Block> modelStateManager;
    private final int worldTimeOffset;
    private ModelState<Block> currentModelState;
    private final Random random = new Random();

    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        MachineBlock machineBlock = (MachineBlock) state.getBlock();
        this.moduleContainer = machineBlock.createModuleContainer(this);
        this.modelStateManager = machineBlock.getModelStateManager();
        this.worldTimeOffset = new Random(getPos().asLong()).nextInt(20);

        this.currentModelState = modelStateManager.getDefaultState();
        refreshModelState();
    }

    public Random getRandom() {
        return random;
    }

    public <T extends MachineModule<?>> Optional<T> getModule(MachineModuleType<?, T> moduleType) {
        return this.moduleContainer.getModule(moduleType);
    }

    @NotNull
    public <T extends MachineModule<?>> T getModuleChecked(MachineModuleType<?, T> moduleType) {
        return getModule(moduleType).orElseThrow(() -> new RuntimeException("Module not found in the machine: " + moduleType));
    }

    @SuppressWarnings("ConstantConditions")
    public long getOffsetWorldTime() {
        return hasWorld() ? (this.worldTimeOffset + getWorld().getTime()) : 0L;
    }

    @NotNull
    public <T> T getExternalAttribute(CombinableAttribute<T> attribute, Direction machineSide) {
        BlockPos targetPos = getPos().offset(machineSide);
        SearchOption<Object> searchOptions = SearchOptions.inDirection(machineSide);

        if (hasWorld()) {
            return attribute.get(getWorld(), targetPos, searchOptions);
        }
        return attribute.defaultValue;
    }

    public static void tick(World world, BlockPos pos, BlockState state, MachineBlockEntity machineBlockEntity) {
        MachineTickType tickType = MachineTickType.of(world);
        machineBlockEntity.moduleContainer.tickModules(tickType);
    }

    public ActionResult attemptSetOrientation(Direction newOrientation, @NotNull LivingEntity player, Simulation simulation) {
        return this.moduleContainer.attemptSetOrientation(newOrientation, player, simulation);
    }

    public Optional<Direction> getOrientation(OrientationKind orientationKind) {
        return this.moduleContainer.getOrientation(orientationKind);
    }

    public void clearInventory(List<ItemStack> droppedStacks, Simulation simulation) {
        this.moduleContainer.clearInventory(droppedStacks, simulation);
    }

    protected void setupModelState(ModelState.Builder<Block> builder) {
        this.moduleContainer.setupModelState(builder);
    }

    @Override
    public void addAllAttributes(AttributeList<?> to) {
        this.moduleContainer.addAllAttributes(to);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("Modules", NbtElement.COMPOUND_TYPE)) {
            NbtCompound modulesNbt = nbt.getCompound("Modules");
            this.moduleContainer.readPersistenceData(modulesNbt);
            this.refreshModelState();
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        NbtCompound modulesNbt = new NbtCompound();
        this.moduleContainer.writePersistenceData(modulesNbt);
        nbt.put("Modules", modulesNbt);

        return nbt;
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        this.moduleContainer.readClientData(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        this.moduleContainer.writeClientData(tag);
        return tag;
    }

    public final void refreshModelStateAndRedraw() {
        if (refreshModelState()) {
            this.scheduleRedrawOnClient();
        }
    }

    private void scheduleRedrawOnClient() {
        World world = getWorld();
        if (world != null && world.isClient()) {
            world.updateListeners(getPos(), getCachedState(), getCachedState(), 0);
        }
    }

    protected final boolean refreshModelState() {
        ModelState<Block> oldModelState = this.currentModelState;

        ModelState.Builder<Block> builder = new ModelState.Builder<>(this.modelStateManager);
        setupModelState(builder);
        this.currentModelState = builder.build();

        return !oldModelState.equals(this.currentModelState);
    }

    @Override
    @Nullable
    public Object getRenderAttachmentData() {
        return new MachineBlockEntityAttachment(this.currentModelState);
    }
}
