package gregtech.api.render.model.part;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import gregtech.api.render.model.CombinedBakeSettings;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;

import java.util.*;
import java.util.function.Function;

public class SimpleModelPart implements ModelPart {
    private final Identifier parentModelId;

    private final AffineTransformation rotation;
    private final boolean uvLock;
    private final Map<String, SpriteIdentifier> textureOverrides;

    private UnbakedModel resultModel;

    public SimpleModelPart(Identifier parentModelId, AffineTransformation rotation, boolean uvLock, Map<String, SpriteIdentifier> textureOverrides) {
        this.parentModelId = parentModelId;
        this.rotation = rotation;
        this.uvLock = uvLock;
        this.textureOverrides = textureOverrides;
    }

    private UnbakedModel createChildModel() {
        return new JsonUnbakedModel(this.parentModelId,
                Collections.emptyList(),
                Maps.transformValues(this.textureOverrides, Either::left),
                true, null,
                ModelTransformation.NONE,
                Collections.emptyList());
    }

    private boolean needsCreateChildModel() {
        return !this.textureOverrides.isEmpty();
    }

    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings bakeSettings, Identifier modelId) {
        CombinedBakeSettings combinedBakeSettings = new CombinedBakeSettings(bakeSettings, rotation, uvLock);
        return this.resultModel.bake(loader, textureGetter, combinedBakeSettings, modelId);
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        if (needsCreateChildModel()) {
            this.resultModel = createChildModel();
            this.resultModel.getTextureDependencies(unbakedModelGetter, new HashSet<>());
        } else {
            this.resultModel = unbakedModelGetter.apply(this.parentModelId);
        }
        return this.textureOverrides.values();
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.singletonList(this.parentModelId);
    }
}
