package gregtech.api.capability.block;

import alexiil.mc.lib.attributes.Simulation;

public interface EnergyContainer {

    /**
     * @return amount of used amperes. 0 if not accepted anything.
     */
    long acceptEnergyFromNetwork(long voltage, long amperage);

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
}