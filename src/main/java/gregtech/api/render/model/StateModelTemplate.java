package gregtech.api.render.model;

import com.google.common.collect.ImmutableList;
import gregtech.api.render.model.component.ModelComponent;
import gregtech.api.render.model.component.ModelComponentTemplate;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;

public class StateModelTemplate {

    private final ImmutableList<ModelComponentTemplate> componentTemplates;

    public StateModelTemplate(Iterable<ModelComponentTemplate> componentTemplates) {
        this.componentTemplates = ImmutableList.copyOf(componentTemplates);
    }

    public StateModel resolve(ModelStateManager<?> stateManager, StateModelManager context, boolean forceStatic) {
        ModelState<?> defaultState = stateManager.getDefaultState();
        ImmutableList.Builder<ModelComponent> components = ImmutableList.builder();

        for (ModelComponentTemplate template : this.componentTemplates) {
            components.addAll(template.resolve(stateManager, context, forceStatic));
        }
        return new StateModel(defaultState, components.build());
    }
}
