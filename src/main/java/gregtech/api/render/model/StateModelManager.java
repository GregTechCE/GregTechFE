package gregtech.api.render.model;

import gregtech.api.render.model.state.ModelStateManager;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface StateModelManager {

    UnbakedModel loadModel(Identifier modelId);

    @Nullable
    StateModel loadStateModel(Identifier modelId, ModelStateManager<?> stateManager, boolean forceStatic);
}
