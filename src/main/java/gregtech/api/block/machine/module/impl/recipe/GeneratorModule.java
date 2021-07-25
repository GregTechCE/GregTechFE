package gregtech.api.block.machine.module.impl.recipe;

import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.archetype.EnergyContainerModule;
import gregtech.api.block.machine.module.impl.config.GeneratorConfig;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.recipe.context.GeneratorMachineContext;
import gregtech.api.recipe.instance.GeneratorRecipeInstance;
import gregtech.api.recipe.instance.impl.GeneratorRecipeInstanceImpl;

public abstract class GeneratorModule<C extends GeneratorConfig, I extends GeneratorRecipeInstance> extends BasicManufacturerModule<C, I> implements GeneratorMachineContext<I> {

    private EnergyContainer energyContainer;

    public GeneratorModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, C config) {
        super(machine, type, config);
    }

    @Override
    public void onModulesReady() {
        super.onModulesReady();
        EnergyContainerModule energyContainerModule = getMachine().getModuleChecked(this.config.getEnergyContainerModuleType());
        this.energyContainer = energyContainerModule.getEnergyContainer();
    }

    public EnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public long getMaxVoltage() {
        return this.energyContainer.getVoltageTier().getVoltage();
    }

    @Override
    public int getTierForBoosting() {
        return this.energyContainer.getVoltageTier().ordinal();
    }

    public static class Impl extends GeneratorModule<GeneratorConfig, GeneratorRecipeInstance> {

        public Impl(MachineBlockEntity machine, MachineModuleType<?, ?> type, GeneratorConfig config) {
            super(machine, type, config);
        }

        @Override
        public GeneratorRecipeInstance createBlankRecipeInstance() {
            return new GeneratorRecipeInstanceImpl<>(getRecipeMap(), this, getEnergyContainer(), this.config.isVoidExcessiveEnergy());
        }
    }
}
