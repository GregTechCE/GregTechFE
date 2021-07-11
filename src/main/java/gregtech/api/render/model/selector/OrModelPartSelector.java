package gregtech.api.render.model.selector;

import com.google.common.collect.ImmutableList;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrModelPartSelector implements ModelPartSelector {

    private final List<ModelPartSelector> selectors;

    public OrModelPartSelector(Iterable<ModelPartSelector> selectors) {
        this.selectors = ImmutableList.copyOf(selectors);
    }

    @Override
    public Predicate<ModelState<?>> resolveSelector(ModelStateManager<?> stateManager) {
        List<Predicate<ModelState<?>>> predicates = this.selectors.stream()
                .map(selector -> selector.resolveSelector(stateManager))
                .collect(Collectors.toList());

        return (blockState) -> {
            for (Predicate<ModelState<?>> predicate : predicates) {
                if (predicate.test(blockState)) {
                    return true;
                }
            }
            return false;
        };
    }
}
