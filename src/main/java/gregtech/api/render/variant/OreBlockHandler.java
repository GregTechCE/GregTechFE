package gregtech.api.render.variant;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import gregtech.api.block.ore.OreBlock;
import gregtech.api.block.ore.VisualOreBlockType;
import gregtech.api.unification.util.MaterialIconSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class OreBlockHandler implements GTModelVariantProvider.BlockModelProvider {

    private static final String ORE_TEXTURE_NAME = "ore";
    private final Map<VisualOreBlockType, UnbakedModel> generatedModels = new HashMap<>();

    @SuppressWarnings("deprecation")
    private UnbakedModel createOreBlockModel(VisualOreBlockType oreBlockType) {
        Identifier blockModelPath = oreBlockType.getOreVariant().getModelPath();

        MaterialIconSet iconSet = oreBlockType.getMaterialIconSet();
        Identifier oreTextureLocation = iconSet.getBlockTextureLocation(ORE_TEXTURE_NAME);
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
    public @Nullable UnbakedModel loadBlockModel(Block block, ModelProviderContext context) {
        if (block instanceof OreBlock oreBlock) {
            VisualOreBlockType oreBlockType = new VisualOreBlockType(oreBlock);
            return this.generatedModels.computeIfAbsent(oreBlockType, this::createOreBlockModel);
        }
        return null;
    }
}
