package gregtech.api.capability.block;

public interface EnergyContainerChangeListener {

    void onChange(EnergyContainer container, long previous, long current);
}
