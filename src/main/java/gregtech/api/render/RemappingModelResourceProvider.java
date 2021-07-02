package gregtech.api.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public enum RemappingModelResourceProvider implements ModelResourceProvider {
    INSTANCE;

    private final Map<ModelIdentifier, Identifier> modelRemapMap = new HashMap<>();

    public void registerItemModel(Item item, Identifier newModelLocation) {
        this.modelRemapMap.put(getItemModelLocation(item), newModelLocation);
    }

    public static ModelIdentifier getItemModelLocation(Item item) {
        return new ModelIdentifier(Registry.ITEM.getId(item), "inventory");
    }

    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) throws ModelProviderException {
        if (resourceId instanceof ModelIdentifier modelIdentifier) {
            Identifier remappedIdentifier = this.modelRemapMap.get(modelIdentifier);
            if (remappedIdentifier != null) {
                return context.loadModel(remappedIdentifier);
            }
        }
        return null;
    }

    static {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(resourceManager -> INSTANCE);
    }
}
