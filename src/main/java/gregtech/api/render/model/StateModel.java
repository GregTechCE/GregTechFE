package gregtech.api.render.model;

import com.google.common.collect.ImmutableList;
import gregtech.api.render.model.component.ModelComponent;
import gregtech.api.render.model.state.ModelState;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StateModel implements UnbakedModel {

    private final ModelState<?> defaultModelState;
    private final ImmutableList<ModelComponent> components;

    public StateModel(ModelState<?> defaultModelState, ImmutableList<ModelComponent> components) {
        this.defaultModelState = defaultModelState;
        this.components = components;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return this.components.stream()
                .flatMap(component -> component.getModelPart().getModelDependencies().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        ArrayList<SpriteIdentifier> outDependencies = new ArrayList<>();

        for (ModelComponent component : components) {
            outDependencies.addAll(component.getModelPart().getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences));
        }
        return outDependencies;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        ImmutableList.Builder<FabricBakedModel> staticComponents = ImmutableList.builder();
        ImmutableList.Builder<Pair<Predicate<ModelState<?>>, FabricBakedModel>> dynamicComponents = ImmutableList.builder();

        for (ModelComponent component : this.components) {
            Predicate<ModelState<?>> predicate = component.getPredicate();

            if (component.isStatic()) {
                if (predicate.test(this.defaultModelState)) {
                    BakedModel bakedModel = component.getModelPart().bake(loader, textureGetter, rotationContainer, modelId);
                    staticComponents.add((FabricBakedModel) bakedModel);
                }
            } else {
                BakedModel bakedModel = component.getModelPart().bake(loader, textureGetter, rotationContainer, modelId);
                dynamicComponents.add(Pair.of(predicate, (FabricBakedModel) bakedModel));
            }
        }
        return new BakedStateModel(this.defaultModelState, staticComponents.build(), dynamicComponents.build());
    }
}
