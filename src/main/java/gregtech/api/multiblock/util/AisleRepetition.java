package gregtech.api.multiblock.util;

public class AisleRepetition {

    public static final AisleRepetition SINGLE = new AisleRepetition(1, 1);
    private final int minRepetitions;
    private final int maxRepetitions;

    public AisleRepetition(int minRepetitions, int maxRepetitions) {
        this.minRepetitions = minRepetitions;
        this.maxRepetitions = maxRepetitions;
    }

    public int getMinRepetitions() {
        return minRepetitions;
    }

    public int getMaxRepetitions() {
        return maxRepetitions;
    }

    public boolean check(int repetitions) {
        return this.minRepetitions <= repetitions && repetitions <= this.maxRepetitions;
    }
}
