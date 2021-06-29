package gregtech.api.unification.material.properties;

public class CableProperties {

    private final int voltage;
    private final int amperage;
    private final int lossPerBlock;

    private CableProperties(int voltage, int amperage, int lossPerBlock) {
        this.voltage = voltage;
        this.amperage = amperage;
        this.lossPerBlock = lossPerBlock;
    }

    public static CableProperties create(int voltage, int amperage, int lossPerBlock) {
        return new CableProperties(voltage, amperage, lossPerBlock);
    }

    public int getVoltage() {
        return voltage;
    }

    public int getAmperage() {
        return amperage;
    }

    public int getLossPerBlock() {
        return lossPerBlock;
    }
}
