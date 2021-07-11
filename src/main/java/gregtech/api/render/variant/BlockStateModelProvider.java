package gregtech.api.render.variant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gregtech.api.block.util.ModelStateProviderBlock;
import gregtech.api.items.util.ModelStateProviderItem;
import gregtech.api.render.model.StateModel;
import gregtech.api.render.model.StateModelDeserializer;
import gregtech.api.render.model.StateModelManager;
import gregtech.api.render.model.StateModelTemplate;
import gregtech.api.render.model.state.ModelStateManager;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BlockStateModelProvider implements GTModelVariantProvider.BlockModelProvider, GTModelVariantProvider.ItemModelProvider {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private final Map<Identifier, StateModelTemplate> loadedStateModels = new HashMap<>();
    private final ResourceManager resourceManager;

    public BlockStateModelProvider(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public @Nullable UnbakedModel loadItemModel(Item item, ModelProviderContext context) throws ModelProviderException {
        if (item instanceof ModelStateProviderItem stateProviderItem) {
            ModelStateManager<?> stateManager = stateProviderItem.getModelStateManager();
            StateModelManagerImpl stateModelManager = new StateModelManagerImpl(context);
            Identifier modelId = stateProviderItem.getStateModelLocation();

            return loadStateModelCached(stateModelManager, modelId, stateManager, false);
        }
        return null;
    }

    @Nullable
    public UnbakedModel loadBlockModel(Block block, ModelProviderContext context) throws ModelProviderException {
        if (block instanceof ModelStateProviderBlock stateProviderBlock) {
            ModelStateManager<?> stateManager = stateProviderBlock.getModelStateManager();
            StateModelManagerImpl stateModelManager = new StateModelManagerImpl(context);
            Identifier modelId = stateProviderBlock.getStateModelLocation();

            return loadStateModelCached(stateModelManager, modelId, stateManager, false);
        }
        return null;
    }

    private StateModelTemplate loadStateModelRaw(Identifier modelId) throws ModelProviderException {
        Identifier fullResourcePath = new Identifier(modelId.getNamespace(), "state_models/" + modelId.getPath());

        try (Resource resource = this.resourceManager.getResource(fullResourcePath)) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
                JsonElement jsonElement = GSON.fromJson(inputStreamReader, JsonElement.class);

                JsonObject jsonObject = JsonHelper.asObject(jsonElement, "root");
                return StateModelDeserializer.deserialize(jsonObject);

            } catch (RuntimeException exception) {
                throw new ModelProviderException("Failed to parse  model " + modelId, exception);
            }
        } catch (IOException exception) {
            throw new ModelProviderException("Failed to load model resource from " + fullResourcePath, exception);
        }
    }

    private StateModel loadStateModelCached(StateModelManager context, Identifier modelId, ModelStateManager<?> stateManager, boolean forceStatic) throws ModelProviderException {
        StateModelTemplate template = this.loadedStateModels.get(modelId);

        if (template == null) {
            template = loadStateModelRaw(modelId);
            this.loadedStateModels.put(modelId, template);
        }
        return template.resolve(stateManager, context, forceStatic);
    }

    private class StateModelManagerImpl implements StateModelManager {

        private final ModelProviderContext modelProviderContext;

        public StateModelManagerImpl(ModelProviderContext modelProviderContext) {
            this.modelProviderContext = modelProviderContext;
        }

        @Override
        public UnbakedModel loadModel(Identifier modelId) {
            return this.modelProviderContext.loadModel(modelId);
        }

        @Override
        public @Nullable StateModel loadStateModel(Identifier modelId, ModelStateManager<?> stateManager, boolean forceStatic) {
            try {
                return BlockStateModelProvider.this.loadStateModelCached(this, modelId, stateManager, forceStatic);
            } catch (ModelProviderException ex) {
                throw new RuntimeException("Failed to load state model " + modelId, ex);
            }
        }
    }
}
