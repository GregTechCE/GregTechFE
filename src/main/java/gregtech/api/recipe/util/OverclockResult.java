package gregtech.api.recipe.util;

public class OverclockResult {

    private final int EUt;
    private final int duration;

    public OverclockResult(int EUt, int duration) {
        this.EUt = EUt;
        this.duration = duration;
    }

    public int getEUt() {
        return EUt;
    }

    public int getDuration() {
        return duration;
    }
}
