package gregtech.api.render.model.part;

import com.mojang.datafixers.util.Pair;
import gregtech.api.render.model.CombinedBakeSettings;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class ModelPartWrapper implements ModelPart {

    private final ModelPart modelPart;
    private final AffineTransformation rotation;
    private final boolean uvLock;

    public ModelPartWrapper(ModelPart modelPart, AffineTransformation rotation, boolean uvLock) {
        this.modelPart = modelPart;
        this.rotation = rotation;
        this.uvLock = uvLock;
    }

    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings bakeSettings, Identifier modelId) {
        CombinedBakeSettings combinedBakeSettings = new CombinedBakeSettings(bakeSettings, this.rotation, this.uvLock);
        return this.modelPart.bake(loader, textureGetter, combinedBakeSettings, modelId);
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return this.modelPart.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences);
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return this.modelPart.getModelDependencies();
    }
}
