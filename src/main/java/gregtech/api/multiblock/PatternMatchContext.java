package gregtech.api.multiblock;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Contains an context used for storing temporary data
 * related to current check and shared between all predicates testing structure itegrity
 */
public interface PatternMatchContext {

    <T> void set(ContextKey<T> contextKey, T value);

    <T> Optional<T> get(ContextKey<T> contextKey);

    <T> T computeIfAbsent(ContextKey<T> contextKey, Supplier<T> constructor);
}
