package gregtech.api.capability.internal;

public class WorkStatus {

    private final int progress;
    private final int maxProgress;
    private final boolean workingEnabled;


    public WorkStatus(int progress, int maxProgress, boolean workingEnabled) {
        this.progress = progress;
        this.maxProgress = maxProgress;
        this.workingEnabled = workingEnabled;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public boolean isWorkingEnabled() {
        return workingEnabled;
    }
}
