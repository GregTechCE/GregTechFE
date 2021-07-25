package gregtech.api.block.machine.module.impl.recipe;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.LimitedFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.impl.SimpleLimitedFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import com.google.common.base.Preconditions;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.api.OrientableMachineModule;
import gregtech.api.block.machine.module.api.OrientationKind;
import gregtech.api.block.machine.module.impl.StandardOrientationKind;
import gregtech.api.block.machine.module.impl.config.SteamManufacturerConfig;
import gregtech.api.recipe.context.ElectricMachineContext;
import gregtech.api.recipe.context.RecipeContext;
import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;
import gregtech.api.recipe.instance.RecipeInstance;
import gregtech.api.recipe.instance.impl.SteamMachineRecipeInstance;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateProperties;
import gregtech.api.util.GTWorldEventUtil;
import gregtech.api.util.VoltageTier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class SteamManufacturerModule<C extends SteamManufacturerConfig, I extends ElectricMachineRecipeInstance> extends BasicManufacturerModule<C, I> implements ElectricMachineContext<I>, OrientableMachineModule {

    private final SimpleFixedFluidInv steamFluidInventory;
    private final LimitedFixedFluidInv limitedSteamFluidInventory;

    private boolean steamExhaustJammed = false;
    private Direction exhaustOrientation = Direction.UP;

    public SteamManufacturerModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, C config) {
        super(machine, type, config);
        this.steamFluidInventory = new SimpleFixedFluidInv(1, this.config.getSteamTankCapacity()) {
            @Override
            public boolean isFluidValidForTank(int tank, FluidKey fluid) {
                return fluid.equals(SteamManufacturerModule.this.config.getSteamFluid());
            }
        };
        this.limitedSteamFluidInventory = new SimpleLimitedFixedFluidInv(this.steamFluidInventory);
        this.limitedSteamFluidInventory.getAllRule().disallowExtraction();
    }

    public boolean isSteamExhaustJammed() {
        return steamExhaustJammed;
    }

    public Direction getExhaustOrientation() {
        return exhaustOrientation;
    }

    public FixedFluidInv getSteamFluidInventory() {
        return steamFluidInventory;
    }

    public void setExhaustOrientation(Direction exhaustOrientation) {
        Preconditions.checkNotNull(exhaustOrientation, "exhaustOrientation");
        this.exhaustOrientation = exhaustOrientation;
        markDirtyAndSync();
    }

    @Override
    public void addAllAttributes(AttributeList<?> attributeList) {
        super.addAllAttributes(attributeList);
        attributeList.offer(this.limitedSteamFluidInventory);
    }

    @Override
    protected void postProcessRecipeInstance(RecipeInstance recipeInstance) {
        recipeInstance.setRecipeDuration((int) Math.ceil(recipeInstance.getRecipeDuration() * this.config.getDurationMultiplier()));
    }

    @Override
    protected void onRecipeFinished(RecipeInstance recipeInstance) {
        exhaustSteam();
    }

    @Override
    protected boolean findAndStartNewRecipe(RecipeContext<?> recipeContext) {
        return exhaustSteamIfJammed() && super.findAndStartNewRecipe(recipeContext);
    }

    protected boolean exhaustSteamIfJammed() {
        if (this.steamExhaustJammed) {
            return exhaustSteam();
        }
        return true;
    }

    protected boolean exhaustSteam() {
        World world = Preconditions.checkNotNull(getMachine().getWorld());
        BlockPos exhaustBlock = getMachine().getPos().offset(this.exhaustOrientation);
        BlockState blockState = world.getBlockState(exhaustBlock);

        if (!blockState.isAir()) {
            this.steamExhaustJammed = true;
            return false;
        }

        if (world instanceof ServerWorld serverWorld) {
            GTWorldEventUtil.emitWorldEvent(serverWorld, GTWorldEventUtil.WORLD_EVENT_STEAM_EXHAUST, getMachine().getPos(), this.exhaustOrientation.getId());
        }
        this.steamExhaustJammed = false;
        return true;
    }

    @Override
    public long getMaxVoltage() {
        return this.config.getMaxVoltage().getVoltage();
    }

    @Override
    public VoltageTier getOverclockingTier() {
        return this.config.getMaxVoltage();
    }

    @Override
    public int getTierForBoosting() {
        return this.config.getMaxVoltage().ordinal();
    }

    @Override
    public OrientationKind getOrientationKind() {
        return StandardOrientationKind.EXHAUST_SIDE;
    }

    @Override
    public Direction getOrientation() {
        return this.exhaustOrientation;
    }

    @Override
    public ActionResult attemptSetOrientation(Direction newOrientation, LivingEntity player, Simulation simulation) {
        if (player.isSneaking()) {
            if (newOrientation != getExhaustOrientation()) {
                setExhaustOrientation(newOrientation);
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    @Override
    public void setupModelState(ModelState.Builder<?> builder) {
        super.setupModelState(builder);
        builder.with(ModelStateProperties.EXHAUST_DIRECTION, this.exhaustOrientation);
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        super.writePersistenceData(nbt);
        nbt.put("SteamTank", this.steamFluidInventory.toTag());
        nbt.putInt("ExhaustOrientation", this.exhaustOrientation.getId());
        nbt.putBoolean("SteamExhaustJammed", this.steamExhaustJammed);
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        super.readPersistenceData(nbt);
        if (nbt.contains("SteamTank", NbtElement.COMPOUND_TYPE)) {
            this.steamFluidInventory.fromTag(nbt.getCompound("SteamTank"));
        }
        if (nbt.contains("ExhaustOrientation", NbtElement.NUMBER_TYPE)) {
            this.exhaustOrientation = Direction.byId(nbt.getInt("ExhaustOrientation"));
        }
        if (nbt.contains("SteamExhaustJammed", NbtElement.NUMBER_TYPE)) {
            this.steamExhaustJammed = nbt.getBoolean("SteamExhaustJammed");
        }
    }

    @Override
    public NbtElement writeSyncData() {
        byte exhaustFacing = (byte) (this.exhaustOrientation.getId() << 1);
        byte resultFlags = (byte) (this.isActive ? 1 : 0);
        return NbtByte.of((byte) (exhaustFacing | resultFlags));
    }

    @Override
    public void readSyncData(NbtElement nbt) {
        byte syncFlags = ((AbstractNbtNumber) nbt).byteValue();
        byte exhaustFacing = (byte) (syncFlags >> 1);
        this.exhaustOrientation = Direction.byId(exhaustFacing);
        this.isActive = (syncFlags & 1) > 0;
    }

    public static class Impl extends SteamManufacturerModule<SteamManufacturerConfig, ElectricMachineRecipeInstance> {

        public Impl(MachineBlockEntity machine, MachineModuleType<?, ?> type, SteamManufacturerConfig config) {
            super(machine, type, config);
        }

        @Override
        public ElectricMachineRecipeInstance createBlankRecipeInstance() {
            return new SteamMachineRecipeInstance<>(getRecipeMap(), this,
                    getSteamFluidInventory().getExtractable(),
                    getConfig().getSteamFluid(),
                    getConfig().getSteamPerEnergyUnit());
        }
    }
}
