package gregtech.api.capability.impl.energy.combined;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.block.EnergyContainerChangeListener;
import gregtech.api.util.VoltageTier;

import java.util.Comparator;
import java.util.List;

public class CombinedEnergyContainer implements EnergyContainer {

    private final List<? extends EnergyContainer> containers;

    public CombinedEnergyContainer(List<? extends EnergyContainer> containers) {
        this.containers = containers;
    }

    @Override
    public long addEnergy(long energyToAdd, Simulation simulate) {
        long energyAdded = 0L;

        for (EnergyContainer container : this.containers) {
            long inserted = container.addEnergy(energyToAdd, simulate);
            energyAdded += inserted;
            energyToAdd -= inserted;
            if (energyToAdd == 0L) break;
        }
        return energyAdded;
    }

    @Override
    public long removeEnergy(long energyToRemove, Simulation simulate) {
        long energyRemoved = 0L;

        for (EnergyContainer container : this.containers) {
            long removed = container.removeEnergy(energyToRemove, simulate);
            energyRemoved += removed;
            energyToRemove -= removed;
            if (energyToRemove == 0L) break;
        }
        return energyRemoved;
    }

    @Override
    public VoltageTier getVoltageTier() {
        return this.containers.stream()
                .map(EnergyContainer::getVoltageTier)
                .min(Comparator.comparing(VoltageTier::getVoltage))
                .orElse(VoltageTier.ULV);
    }

    @Override
    public long getEnergyStored() {
        return this.containers.stream()
                .mapToLong(EnergyContainer::getEnergyStored)
                .sum();
    }

    @Override
    public long getEnergyCapacity() {
        return this.containers.stream()
                .mapToLong(EnergyContainer::getEnergyCapacity)
                .sum();
    }

    @Override
    public ListenerToken addListener(EnergyContainerChangeListener listener, ListenerRemovalToken removalToken) {
        final ListenerToken[] tokens = new ListenerToken[this.containers.size()];

        final ListenerRemovalToken ourRemToken = new ListenerRemovalToken() {
            boolean hasAlreadyRemoved = false;

            @Override
            public void onListenerRemoved() {
                for (ListenerToken token : tokens) {
                    if (token == null) {
                        return;
                    }
                    token.removeListener();
                }
                if (!hasAlreadyRemoved) {
                    hasAlreadyRemoved = true;
                    removalToken.onListenerRemoved();
                }
            }
        };

        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = this.containers.get(i).addListener(listener, ourRemToken);
            if (tokens[i] == null) {
                for (int j = 0; j < i; j++) {
                    tokens[j].removeListener();
                }
                return null;
            }
        }

        return () -> {
            for (ListenerToken token : tokens) {
                token.removeListener();
            }
        };
    }
}
