package gregtech.api.capability.impl.energy;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.Saveable;
import com.google.common.base.Preconditions;
import gregtech.api.capability.block.EnergyContainer;
import gregtech.api.capability.block.EnergyContainerChangeListener;
import gregtech.api.util.VoltageTier;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenCustomHashMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;

import java.util.Map;

public class SimpleEnergyContainer implements EnergyContainer, Saveable {

    private final Map<EnergyContainerChangeListener, ListenerRemovalToken> listeners = new Object2ObjectLinkedOpenCustomHashMap<>(Util.identityHashStrategy());
    private final long capacity;
    private final VoltageTier voltageTier;
    private long energyStored;

    public SimpleEnergyContainer(long capacity, VoltageTier voltageTier) {
        this.capacity = capacity;
        this.voltageTier = voltageTier;
    }

    @Override
    public long addEnergy(long energyToAdd, Simulation simulate) {
        long energyAdded = Math.min(energyToAdd, this.capacity - this.energyStored);
        if (simulate.isAction()) {
            long previous = this.energyStored;
            this.energyStored += energyAdded;
            fireEnergyChanged(previous);
        }
        return energyAdded;
    }

    @Override
    public long removeEnergy(long energyToRemove, Simulation simulate) {
        long energyRemoved = Math.min(this.energyStored, energyToRemove);
        if (simulate.isAction()) {
            long previous = this.energyStored;
            this.energyStored -= energyRemoved;
            fireEnergyChanged(previous);
        }
        return energyRemoved;
    }

    private void fireEnergyChanged(long previousEnergy) {
        for (EnergyContainerChangeListener listener : this.listeners.keySet()) {
            listener.onChange(this, previousEnergy, this.energyStored);
        }
    }

    @Override
    public long getEnergyStored() {
        return this.energyStored;
    }

    @Override
    public long getEnergyCapacity() {
        return this.capacity;
    }

    @Override
    public VoltageTier getVoltageTier() {
        return this.voltageTier;
    }

    @Override
    public ListenerToken addListener(EnergyContainerChangeListener listener, ListenerRemovalToken removalToken) {
        ListenerRemovalToken previous = listeners.put(listener, removalToken);
        Preconditions.checkArgument(previous == null || previous == removalToken, "The same listener object must be registered with the same removal token");

        return () -> {
            ListenerRemovalToken token = listeners.remove(listener);
            if (token != null) {
                token.onListenerRemoved();
            }
        };
    }

    public void invalidateListeners() {
        this.listeners.values().forEach(ListenerRemovalToken::onListenerRemoved);
        this.listeners.clear();
    }

    @Override
    public NbtCompound toTag(NbtCompound tag) {
        tag.putLong("energy_stored", this.energyStored);
        return tag;
    }

    @Override
    public void fromTag(NbtCompound tag) {

    }
}
