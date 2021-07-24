package gregtech.api.unification.material.properties;

import gregtech.api.util.VoltageTier;

public class CableProperties {

    private final VoltageTier voltageTier;
    private final int amperage;
    private final int lossPerBlock;

    private CableProperties(VoltageTier voltageTier, int amperage, int lossPerBlock) {
        this.voltageTier = voltageTier;
        this.amperage = amperage;
        this.lossPerBlock = lossPerBlock;
    }

    public static CableProperties create(VoltageTier voltageTier, int amperage, int lossPerBlock) {
        return new CableProperties(voltageTier, amperage, lossPerBlock);
    }

    public int getVoltage() {
        return voltageTier.getVoltage();
    }

    public int getAmperage() {
        return amperage;
    }

    public int getLossPerBlock() {
        return lossPerBlock;
    }
}
