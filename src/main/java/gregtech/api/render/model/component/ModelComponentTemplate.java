package gregtech.api.render.model.component;

import gregtech.api.render.model.StateModelManager;
import gregtech.api.render.model.function.ModelComponentFunction;
import gregtech.api.render.model.part.ModelPart;
import gregtech.api.render.model.part.ModelPartFactory;
import gregtech.api.render.model.selector.ModelPartSelector;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;

import java.util.function.Predicate;

public class ModelComponentTemplate {

    private final ModelPartFactory partFactory;
    private final ModelPartSelector selector;
    private final ModelComponentFunction function;
    private final boolean isStatic;

    public ModelComponentTemplate(ModelPartFactory partFactory, ModelPartSelector selector, ModelComponentFunction function, boolean isStatic) {
        this.partFactory = partFactory;
        this.selector = selector;
        this.function = function;
        this.isStatic = isStatic;
    }

    public Iterable<ModelComponent> resolve(ModelStateManager<?> stateManager, StateModelManager context, boolean forceStatic) {
        ModelPart modelPart = this.partFactory.create(stateManager, context, forceStatic);
        Predicate<ModelState<?>> predicate = this.selector.resolveSelector(stateManager);
        boolean resultIsStatic = this.isStatic || forceStatic;

        ModelComponent modelComponent = new ModelComponent(modelPart, predicate, resultIsStatic);
        return this.function.apply(modelComponent, stateManager, context);
    }
}
