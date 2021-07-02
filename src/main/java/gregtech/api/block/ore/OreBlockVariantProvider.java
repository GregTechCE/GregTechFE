package gregtech.api.block.ore;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import gregtech.api.render.RemappingModelResourceProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public enum OreBlockVariantProvider implements ModelVariantProvider {
    INSTANCE;

    private static final String ORE_TEXTURE_NAME = "ore";
    private final Map<ModelIdentifier, VisualOreBlockType> oreBlockTypeMap = new HashMap<>();
    private final Map<VisualOreBlockType, UnbakedModel> generatedModels = new HashMap<>();

    public void registerOreBlock(BlockState blockState, VisualOreBlockType blockType) {
        this.oreBlockTypeMap.put(BlockModels.getModelId(blockState), blockType);
    }

    public void registerOreBlockItem(Item item, VisualOreBlockType blockType) {
        this.oreBlockTypeMap.put(RemappingModelResourceProvider.getItemModelLocation(item), blockType);
    }

    @SuppressWarnings("deprecation")
    private UnbakedModel createOreBlockModel(VisualOreBlockType blockType) {
        Identifier blockModelPath = blockType.getOreVariant().getModelPath();

        Identifier oreTextureLocation = blockType.getMaterialIconSet().getBlockTextureLocation(ORE_TEXTURE_NAME);
        SpriteIdentifier textureId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, oreTextureLocation);

        return new JsonUnbakedModel(blockModelPath,
                Collections.emptyList(),
                ImmutableMap.of("ore_texture", Either.left(textureId)),
                true,
                null,
                ModelTransformation.NONE,
                Collections.emptyList());
    }

    @Override
    public @Nullable UnbakedModel loadModelVariant(ModelIdentifier modelId, ModelProviderContext context) {
        VisualOreBlockType oreBlockType = oreBlockTypeMap.get(modelId);

        if (oreBlockType != null) {
            return this.generatedModels.computeIfAbsent(oreBlockType, this::createOreBlockModel);
        }
        return null;
    }

    static {
        ModelLoadingRegistry.INSTANCE.registerVariantProvider((resourceManager) -> INSTANCE);
    }
}
