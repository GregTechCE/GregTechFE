package gregtech.api.capability.internal;

import java.util.Optional;

/**
 * For machines which have progress and can work
 */
public interface Workable {

    Optional<WorkStatus> getWorkStatus();
}