package gregtech.api.capability.internal;

/**
 * For machines which have progress and can work
 */
public interface Workable extends Controllable {

    /**
     * @return current progress of machine
     */
    int getProgress();

    /**
     * @return progress machine need to complete it's stuff
     */
    int getMaxProgress();

    /**
     * @return true is machine is active
     */
    boolean isActive();
}