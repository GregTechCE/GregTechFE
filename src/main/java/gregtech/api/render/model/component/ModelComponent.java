package gregtech.api.render.model.component;

import gregtech.api.render.model.part.ModelPart;
import gregtech.api.render.model.state.ModelState;

import java.util.function.Predicate;

public class ModelComponent {

    private final ModelPart modelPart;
    private final Predicate<ModelState<?>> predicate;
    private final boolean isStatic;

    public ModelComponent(ModelPart modelPart, Predicate<ModelState<?>> predicate, boolean isStatic) {
        this.modelPart = modelPart;
        this.predicate = predicate;
        this.isStatic = isStatic;
    }

    public ModelPart getModelPart() {
        return modelPart;
    }

    public Predicate<ModelState<?>> getPredicate() {
        return predicate;
    }

    public boolean isStatic() {
        return isStatic;
    }
}
