package gregtech.api.render.model.part;

import gregtech.api.render.model.StateModel;
import gregtech.api.render.model.StateModelManager;
import gregtech.api.render.model.state.ModelStateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;

public class VariantModelPartFactory implements ModelPartFactory {

    private final Identifier parentVariantId;
    private final AffineTransformation rotation;
    private final boolean uvLock;

    public VariantModelPartFactory(Identifier parentVariantId, AffineTransformation rotation, boolean uvLock) {
        this.parentVariantId = parentVariantId;
        this.rotation = rotation;
        this.uvLock = uvLock;
    }

    @Override
    public ModelPart create(ModelStateManager<?> stateManager, StateModelManager context, boolean forceStatic) {
        StateModel stateModel = context.loadStateModel(this.parentVariantId, stateManager, forceStatic);

        if (stateModel == null) {
            throw new RuntimeException("Failed to load parent state model at " + this.parentVariantId);
        }
        return new VariantModelPart(stateModel, rotation, uvLock);
    }
}
