package gregtech.api.block.machine.module.impl;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.module.MachineModule;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.api.AttributeProviderModule;
import gregtech.api.block.machine.module.api.PersistentMachineModule;
import gregtech.api.block.machine.module.impl.archetype.EnergyContainerModule;
import gregtech.api.block.machine.module.impl.config.EnergyConsumerConfig;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.block.EnergySink;
import gregtech.api.capability.impl.energy.SimpleEnergyContainer;
import gregtech.api.util.OvervoltageHelper;
import gregtech.api.util.VoltageTier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class EnergyConsumerModule extends MachineModule<EnergyConsumerConfig> implements EnergyContainerModule, AttributeProviderModule, PersistentMachineModule {

    private final SimpleEnergyContainer energyContainer;
    private final EnergySink energySink;

    public EnergyConsumerModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, EnergyConsumerConfig config) {
        super(machine, type, config);
        this.energyContainer = new SimpleEnergyContainer(config.getCapacity(), config.getTier());
        this.energySink = new WrappedEnergySink();
    }

    protected int getMaxInputAmperage() {
        return this.config.getMaxInputAmperage();
    }

    @Override
    public void addAllAttributes(AttributeList<?> attributeList) {
        attributeList.offer(this.energyContainer);
        attributeList.offer(this.energySink);
    }

    @Override
    public EnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public void writePersistenceData(NbtCompound nbt) {
        nbt.put("EnergyContainer", this.energyContainer.toTag());
    }

    @Override
    public void readPersistenceData(NbtCompound nbt) {
        if (nbt.contains("EnergyContainer", NbtElement.COMPOUND_TYPE)) {
            this.energyContainer.fromTag(nbt.getCompound("EnergyContainer"));
        }
    }

    protected class WrappedEnergySink implements EnergySink {

        @Override
        public VoltageTier getVoltageTier() {
            return energyContainer.getVoltageTier();
        }

        @Override
        public int acceptEnergyFromNetwork(int voltage, int amperage, Simulation simulation) {
            VoltageTier voltageTier = energyContainer.getVoltageTier();

            if (voltageTier.getVoltage() < voltage) {
                if (simulation.isAction()) {
                    OvervoltageHelper.doOvervoltageExplosion(getMachine(), voltageTier);
                }
                return 0;
            }

            amperage = Math.min(amperage, getMaxInputAmperage());

            long totalEnergyToAccept = (long) voltage * amperage;
            long energyAccepted = energyContainer.addEnergy(totalEnergyToAccept, Simulation.SIMULATE);

            int amperageAccepted = (int) (energyAccepted / voltage);

            long actualEnergyToAccept = (long) voltage * amperageAccepted;
            long energyActuallyAccepted = energyContainer.addEnergy(actualEnergyToAccept, Simulation.SIMULATE);

            if (actualEnergyToAccept == energyActuallyAccepted) {
                if (simulation.isAction()) {
                    energyContainer.addEnergy(actualEnergyToAccept, Simulation.ACTION);
                }
                return amperageAccepted;
            }
            return 0;
        }
    }
}
