package gregtech.api.block.machine.module.impl.recipe;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.item.FixedItemInv;
import com.google.common.base.Preconditions;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.api.*;
import gregtech.api.block.machine.module.impl.archetype.IOInventoryModule;
import gregtech.api.block.machine.module.impl.config.ManufacturerConfig;
import gregtech.api.capability.internal.Controllable;
import gregtech.api.capability.internal.WorkStatus;
import gregtech.api.capability.internal.Workable;
import gregtech.api.recipe.GTRecipeManager;
import gregtech.api.recipe.MachineRecipe;
import gregtech.api.recipe.MachineRecipeType;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.context.RecipeContext;
import gregtech.api.recipe.instance.RecipeInstance;
import gregtech.api.recipe.instance.impl.RecipeInstanceImpl;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateProperties;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Optional;
import java.util.Random;

public abstract class BasicManufacturerModule<C extends ManufacturerConfig, I extends RecipeInstance> extends MachineModule<C> implements PersistentMachineModule, SyncedMachineModule, ModelStateAwareModule, TickableMachineModule, AttributeProviderModule, RecipeContext<I> {

    private RecipeMap recipeMap;
    private IOInventoryModule inventoryModule;

    private boolean workingEnabled;
    private RecipeInstance recipeInstance;

    private boolean recipeSearchFailed;
    protected boolean isActive;

    private final Workable workable = new RecipeBackedWorkable();
    private final Controllable controllable = new RecipeBackedControllable();

    public BasicManufacturerModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, C config) {
        super(machine, type, config);
        setupRecipeMap();
    }

    public RecipeMap getRecipeMap() {
        return recipeMap;
    }

    public RecipeInstance getRecipeInstance() {
        return recipeInstance;
    }

    public boolean isWorkingEnabled() {
        return this.workingEnabled;
    }

    public void setWorkingEnabled(boolean workingEnabled) {
        this.workingEnabled = workingEnabled;
        markDirty();
    }

    private void setupRecipeMap() {
        GTRecipeManager recipeManager = GTRecipeManager.get(machine.getWorld());
        Preconditions.checkNotNull(recipeManager, "recipeManager not found for the world");

        MachineRecipeType recipeType = this.config.getRecipeType();
        this.recipeMap = recipeManager.getRecipesOfType(recipeType);
    }

    @Override
    public void onModulesReady() {
        this.inventoryModule = getMachine().getModuleChecked(this.config.getInventoryModuleType());

        this.inventoryModule.addInputChangeListener(this::scheduleRecipeSearch);
        this.inventoryModule.addOutputChangeListener(this::refreshOutputStatus);

        this.inventoryModule.setImportFluidFilter(recipeMap::canInputFluid);
        this.inventoryModule.setImportItemFilter(recipeMap::canInputItem);
    }

    @Override
    public void addAllAttributes(AttributeList<?> attributeList) {
        attributeList.offer(this.workable);
        attributeList.offer(this.controllable);
    }

    public void scheduleRecipeSearch() {
        this.recipeSearchFailed = false;
    }

    public void refreshOutputStatus() {
        if (this.recipeInstance != null) {
            this.recipeInstance.recheckRecipeOutputs();
        }
    }

    @Override
    public void tick(MachineTickType tickType) {
        if (tickType.isServer()) {
            serverTick();
        }
    }

    protected void serverTick() {
        if (this.recipeInstance != null && isWorkingEnabled()) {
            this.recipeInstance.tickRecipe();

            if (this.recipeInstance.hasRecipeFinished()) {
                onRecipeFinished(this.recipeInstance);
                this.recipeInstance = null;
                markDirty();
            }
        }

        if (this.recipeInstance == null) {
            if (findAndStartNewRecipe(this)) {
                markDirty();
            }
        }

        boolean shouldBeActive = this.recipeInstance != null;
        if (this.isActive != shouldBeActive) {
            setActive(shouldBeActive);
        }
    }

    protected void postProcessRecipeInstance(RecipeInstance recipeInstance) {
    }

    protected void onRecipeFinished(RecipeInstance recipeInstance) {
    }

    protected boolean findAndStartNewRecipe(RecipeContext<?> recipeContext) {
        if (this.recipeSearchFailed) {
            return false;
        }

        MachineRecipe<?, ?> machineRecipe = this.recipeMap.findRecipe(recipeContext);
        if (machineRecipe == null) {
            this.recipeSearchFailed = true;
            return false;
        }

        this.recipeInstance = startRecipeChecked(machineRecipe, recipeContext);
        Preconditions.checkNotNull(recipeInstance);
        postProcessRecipeInstance(this.recipeInstance);
        return true;
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        nbt.putBoolean("WorkingEnabled", this.workingEnabled);

        if (this.recipeInstance != null) {
            nbt.put("Recipe", this.recipeInstance.toTag());
        }
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        if (nbt.contains("WorkingEnabled", NbtElement.NUMBER_TYPE)) {
            this.workingEnabled = nbt.getBoolean("WorkingEnabled");
        }

        if (nbt.contains("Recipe", NbtElement.COMPOUND_TYPE)) {
            NbtCompound recipeData = nbt.getCompound("Recipe");
            RecipeInstance recipeInstance = loadRecipeInstance(recipeData);

            if (recipeInstance != null && recipeInstance.isValid()) {
                this.recipeInstance = recipeInstance;
                this.isActive = true;
            }
        }
    }

    protected void setActive(boolean isActive) {
        this.isActive = isActive;
        markDirtyAndSync();
    }

    @Override
    public void setupModelState(ModelState.Builder<?> builder) {
        builder.with(ModelStateProperties.ACTIVE, this.isActive);
    }

    @Override
    public NbtElement writeSyncData() {
        return NbtByte.of((byte) (isActive ? 1 : 0));
    }

    @Override
    public void readSyncData(NbtElement nbt) {
        this.isActive = ((NbtByte) nbt).byteValue() > 0;
    }

    @Override
    public Random getRandom() {
        return getMachine().getRandom();
    }

    @Override
    public FixedItemInv getItemInventory() {
        return this.inventoryModule.getItemImportInventory();
    }

    @Override
    public FixedFluidInv getFluidInventory() {
        return this.inventoryModule.getFluidImportInventory();
    }

    @Override
    public FixedItemInv getOutputItemInventory() {
        return this.inventoryModule.getItemExportInventory();
    }

    @Override
    public FixedFluidInv getOutputFluidInventory() {
        return this.inventoryModule.getFluidExportInventory();
    }

    @SuppressWarnings("unchecked")
    private static <A extends RecipeContext<?>> RecipeInstance startRecipeChecked(MachineRecipe<A, ?> recipe, RecipeContext<?> context) {
        Preconditions.checkArgument(recipe.getMinimumSupportedContextClass().isAssignableFrom(context.getClass()));
        return recipe.startRecipe((A) context);
    }

    protected class RecipeBackedControllable implements Controllable {

        @Override
        public boolean isWorkingEnabled() {
            return BasicManufacturerModule.this.isWorkingEnabled();
        }

        @Override
        public void setWorkingEnabled(boolean isActivationAllowed) {
            BasicManufacturerModule.this.setWorkingEnabled(isActivationAllowed);
        }
    }

    protected class RecipeBackedWorkable implements Workable {

        @Override
        public Optional<WorkStatus> getWorkStatus() {
            RecipeInstance recipeInstance = getRecipeInstance();
            boolean workingEnabled = isWorkingEnabled();

            if (recipeInstance == null) {
                return Optional.empty();
            }
            return Optional.of(new WorkStatus(
                    recipeInstance.getRecipeProgress(),
                    recipeInstance.getRecipeDuration(),
                    workingEnabled));
        }
    }

    public static class Impl extends BasicManufacturerModule<ManufacturerConfig, RecipeInstance> {

        public Impl(MachineBlockEntity machine, MachineModuleType<?, ?> type, ManufacturerConfig config) {
            super(machine, type, config);
        }

        @Override
        public RecipeInstance createBlankRecipeInstance() {
            return new RecipeInstanceImpl<>(getRecipeMap(), this);
        }

        @Override
        public int getTierForBoosting() {
            return 0;
        }
    }
}
