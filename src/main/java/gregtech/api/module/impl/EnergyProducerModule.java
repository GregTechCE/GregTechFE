package gregtech.api.module.impl;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.block.machine.MachineBlockEntity;
import gregtech.api.block.machine.MachineTickType;
import gregtech.api.module.MachineModule;
import gregtech.api.module.MachineModuleType;
import gregtech.api.module.api.AttributeProviderModule;
import gregtech.api.module.api.PersistentMachineModule;
import gregtech.api.module.api.TickableMachineModule;
import gregtech.api.module.impl.archetype.EnergyContainerModule;
import gregtech.api.module.impl.config.EnergyProducerConfig;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.block.EnergySink;
import gregtech.api.capability.block.EnergySource;
import gregtech.api.capability.impl.energy.SimpleEnergyContainer;
import gregtech.api.util.EnergyNetworkUtil;
import gregtech.api.util.VoltageTier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class EnergyProducerModule extends MachineModule<EnergyProducerConfig> implements EnergyContainerModule, PersistentMachineModule, AttributeProviderModule, TickableMachineModule {

    private final SimpleEnergyContainer energyContainer;
    private final EnergySource energySource;

    public EnergyProducerModule(MachineBlockEntity machine, MachineModuleType<?, ?> type, EnergyProducerConfig config) {
        super(machine, type, config);
        this.energyContainer = new SimpleEnergyContainer(config.getCapacity(), config.getTier());
        this.energySource = new WrappedEnergySource();
    }

    protected int getMaxOutputAmperage() {
        return this.config.getMaxOutputAmperage();
    }

    protected Optional<Direction> getOutputDirection() {
        return getMachine().getOrientation(this.config.getOutputDirection());
    }

    protected boolean canExposeEnergySource(Direction direction) {
        Optional<Direction> outputDirection = getOutputDirection();
        return direction == null || outputDirection.isPresent() && outputDirection.get() == direction;
    }

    @Override
    public void addAllAttributes(AttributeList<?> attributeList) {
        attributeList.offer(this.energyContainer);

        if (canExposeEnergySource(attributeList.getTargetSide())) {
            attributeList.offer(this.energySource);
        }
    }

    @Override
    public void tick(MachineTickType tickType) {
        if (tickType.isServer()) {
            serverTick();
        }
    }

    protected void serverTick() {
        Optional<Direction> outputDirection = getOutputDirection();
        boolean hasEnoughEnergy = this.energyContainer.getEnergyStored() >= this.energyContainer.getVoltageTier().getVoltage();

        if (hasEnoughEnergy && outputDirection.isPresent()) {
            EnergySink energySink = getMachine().getExternalAttribute(GTAttributes.ENERGY_SINK, outputDirection.get());

            if (!(energySink instanceof NullVariant)) {
                EnergyNetworkUtil.transferEnergy(this.energySource, energySink);
            }
        }
    }

    @Override
    public EnergyContainer getEnergyContainer() {
        return this.energyContainer;
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

    protected class WrappedEnergySource implements EnergySource {

        @Override
        public VoltageTier getVoltageTier() {
            return energyContainer.getVoltageTier();
        }

        @Override
        public int pullEnergyIntoNetwork(int voltage, int amperage, Simulation simulation) {
            amperage = Math.min(amperage, getMaxOutputAmperage());

            long totalEnergyToRemove = (long) voltage * amperage;
            long energyRemoved = energyContainer.removeEnergy(totalEnergyToRemove, Simulation.SIMULATE);

            int amperageRemoved = (int) (energyRemoved / voltage);

            long actualEnergyToRemove = (long) voltage * amperageRemoved;
            long energyActuallyRemoved = energyContainer.removeEnergy(actualEnergyToRemove, Simulation.SIMULATE);

            if (energyActuallyRemoved == actualEnergyToRemove) {
                if (simulation.isAction()) {
                    energyContainer.removeEnergy(actualEnergyToRemove, Simulation.ACTION);
                }
                return amperageRemoved;
            }
            return 0;
        }
    }
}
