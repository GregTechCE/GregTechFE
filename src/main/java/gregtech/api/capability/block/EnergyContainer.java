package gregtech.api.capability.block;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.util.VoltageTier;

public interface EnergyContainer {

    /**
     * Adds specified amount of energy to this energy container
     * @param energyToAdd amount of energy to add
     * @param simulate whenever action should be simulated or not
     * @return amount of energy added
     */
    long addEnergy(long energyToAdd, Simulation simulate);

    /**
     * Removes specified amount of energy from this energy container
     * @param energyToRemove amount of energy to remove
     * @param simulate whenever action should be simulated or not
     * @return amount of energy removed
     */
    long removeEnergy(long energyToRemove, Simulation simulate);

    /**
     * Gets the stored electric energy.
     */
    long getEnergyStored();

    /**
     * Gets the largest electric energy capacity
     */
    long getEnergyCapacity();

    /**
     * Returns the maximum voltage supported by this energy container
     * Used for automatically filtering maximum energy throughput
     * @return voltage tier of this energy container
     */
    VoltageTier getVoltageTier();

    /**
     * Adds a listener to this energy container which will be fired when energy amount inside of it has charged
     * Inventory can return null if it doesn't support adding listeners, that feature is completely optional
     *
     * @param listener listener object to add
     * @param removalToken removal token fired when listener is removed from the container
     * @return token for removing the listener if added, or null if listener hasn't been added
     */
    ListenerToken addListener(EnergyContainerChangeListener listener, ListenerRemovalToken removalToken);
}