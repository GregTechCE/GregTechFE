package gregtech.api.capability;

import net.minecraft.util.math.Direction;

public interface EnergyContainer {

    /**
     * @return amount of used amperes. 0 if not accepted anything.
     */
    long acceptEnergyFromNetwork(Direction side, long voltage, long amperage);

    /**
     * Adds specified amount of energy to this energy container
     * @param energyToAdd amount of energy to add
     * @return amount of energy added
     */
    long addEnergy(long energyToAdd);

    /**
     * Removes specified amount of energy from this energy container
     * @param energyToRemove amount of energy to remove
     * @return amount of energy removed
     */
    long removeEnergy(long energyToRemove);

    /**
     * Gets the stored electric energy
     */
    long getEnergyStored();

    /**
     * Gets the largest electric energy capacity
     */
    long getEnergyCapacity();

    default long getEnergyCanBeInserted() {
        return getEnergyCapacity() - getEnergyStored();
    }
}