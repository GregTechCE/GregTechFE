package gregtech.api.render.model.selector;

import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;

import java.util.function.Predicate;

@FunctionalInterface
public interface ModelPartSelector {

    ModelPartSelector TRUE = (stateManager) -> (state) -> true;
    ModelPartSelector FALSE = (stateManager) -> (state) -> false;

    Predicate<ModelState<?>> resolveSelector(ModelStateManager<?> stateManager);

}
