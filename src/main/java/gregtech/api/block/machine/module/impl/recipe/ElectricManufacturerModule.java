package gregtech.api.block.machine.module.impl.recipe;

import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.archetype.EnergyContainerModule;
import gregtech.api.block.machine.module.impl.config.ElectricManufacturerConfig;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.recipe.context.ElectricMachineContext;
import gregtech.api.recipe.instance.ElectricMachineRecipeInstance;
import gregtech.api.recipe.instance.impl.ElectricMachineRecipeInstanceImpl;
import gregtech.api.util.VoltageTier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public abstract class ElectricManufacturerModule<C extends ElectricManufacturerConfig, I extends ElectricMachineRecipeInstance> extends BasicManufacturerModule<C, I> implements ElectricMachineContext<I> {

    private EnergyContainer energyContainer;
    private VoltageTier overclockingTier;

    public ElectricManufacturerModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, C config) {
        super(machine, type, config);
    }

    @Override
    public void onModulesReady() {
        super.onModulesReady();
        EnergyContainerModule energyContainerModule = getMachine().getModuleChecked(this.config.getEnergyContainerModule());
        this.energyContainer = energyContainerModule.getEnergyContainer();
        this.overclockingTier = energyContainer.getVoltageTier();
    }

    public EnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    public VoltageTier getMaximumOverclockingTier() {
        return this.energyContainer.getVoltageTier();
    }

    @Override
    public VoltageTier getOverclockingTier() {
        return overclockingTier;
    }

    public void setOverclockingTier(VoltageTier tier) {
        VoltageTier machineTier = getMaximumOverclockingTier();
        this.overclockingTier = tier.getVoltage() >= machineTier.getVoltage() ? machineTier : tier;
        markDirty();
    }

    @Override
    public long getMaxVoltage() {
        return this.energyContainer.getVoltageTier().getVoltage();
    }

    @Override
    public int getTierForBoosting() {
        return this.energyContainer.getVoltageTier().ordinal();
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        super.writePersistenceData(nbt);
        nbt.putString("OverclockingTier", this.overclockingTier.getName());
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        super.readPersistenceData(nbt);

        if (nbt.contains("OverclockingTier", NbtElement.STRING_TYPE)) {
            VoltageTier tier = VoltageTier.byName(nbt.getString("OverclockingTier"));

            if (tier != null) {
                this.overclockingTier = tier;
            }
        }
    }

    public static class Impl extends ElectricManufacturerModule<ElectricManufacturerConfig, ElectricMachineRecipeInstance> {

        public Impl(MachineBlockEntity machine, MachineModuleType<?, ?> type, ElectricManufacturerConfig config) {
            super(machine, type, config);
        }

        @Override
        public ElectricMachineRecipeInstance createBlankRecipeInstance() {
            return new ElectricMachineRecipeInstanceImpl<>(getRecipeMap(), this, getEnergyContainer());
        }
    }
}
