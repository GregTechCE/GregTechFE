package gregtech.api.render.model.function;

import gregtech.api.render.model.StateModelManager;
import gregtech.api.render.model.component.ModelComponent;
import gregtech.api.render.model.state.ModelStateManager;

import java.util.Collections;

@FunctionalInterface
public interface ModelComponentFunction {
    ModelComponentFunction IDENTITY = (component, stateManager, modelManager) -> Collections.singleton(component);

    Iterable<ModelComponent> apply(ModelComponent component, ModelStateManager<?> stateManager, StateModelManager modelManager);
}
