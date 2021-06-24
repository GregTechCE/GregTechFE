package gregtech.api.capability.impl;

import gregtech.api.capability.EnergyContainer;
import net.minecraft.util.EnumFacing;

import java.util.List;

public class EnergyContainerList implements EnergyContainer {

    private List<EnergyContainer> energyContainerList;

    public EnergyContainerList(List<EnergyContainer> energyContainerList) {
        this.energyContainerList = energyContainerList;
    }

    @Override
    public long acceptEnergyFromNetwork(EnumFacing side, long voltage, long amperage) {
        long amperesUsed = 0L;
        for (EnergyContainer energyContainer : energyContainerList) {
            amperesUsed += energyContainer.acceptEnergyFromNetwork(null, voltage, amperage);
            if (amperage == amperesUsed) break;
        }
        return amperesUsed;
    }

    @Override
    public long changeEnergy(long energyToAdd) {
        long energyAdded = 0L;
        for (EnergyContainer energyContainer : energyContainerList) {
            energyAdded += energyContainer.changeEnergy(energyToAdd - energyAdded);
            if (energyAdded == energyToAdd) break;
        }
        return energyAdded;
    }

    @Override
    public long getEnergyStored() {
        return energyContainerList.stream()
            .mapToLong(EnergyContainer::getEnergyStored)
            .sum();
    }

    @Override
    public long getEnergyCapacity() {
        return energyContainerList.stream()
            .mapToLong(EnergyContainer::getEnergyCapacity)
            .sum();
    }

    @Override
    public long getInputAmperage() {
        return 1L;
    }

    @Override
    public long getOutputAmperage() {
        return 1L;
    }

    @Override
    public long getInputVoltage() {
        return energyContainerList.stream()
            .mapToLong(v -> v.getInputVoltage() * v.getInputAmperage())
            .sum();
    }

    @Override
    public long getOutputVoltage() {
        return energyContainerList.stream()
            .mapToLong(v -> v.getOutputVoltage() * v.getOutputAmperage())
            .sum();
    }

    @Override
    public boolean inputsEnergy(EnumFacing side) {
        return true;
    }

    @Override
    public boolean outputsEnergy(EnumFacing side) {
        return true;
    }
}
