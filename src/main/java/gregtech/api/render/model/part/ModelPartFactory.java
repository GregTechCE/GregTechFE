package gregtech.api.render.model.part;

import gregtech.api.render.model.StateModelManager;
import gregtech.api.render.model.state.ModelStateManager;

@FunctionalInterface
public interface ModelPartFactory {
    ModelPart create(ModelStateManager<?> stateManager, StateModelManager context, boolean forceStatic);

    static ModelPartFactory identity(ModelPart component) {
        return (stateManager, context, forceStatic) -> component;
    }
}
