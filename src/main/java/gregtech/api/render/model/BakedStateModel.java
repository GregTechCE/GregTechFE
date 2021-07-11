package gregtech.api.render.model;

import com.google.common.collect.ImmutableList;
import gregtech.api.block.util.ModelStateProviderBlock;
import gregtech.api.items.util.ModelStateProviderItem;
import gregtech.api.render.model.state.ModelState;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BakedStateModel implements BakedModel, FabricBakedModel {

    private final ModelState<?> defaultModelState;
    private final BakedModel masterModel;

    private final ImmutableList<FabricBakedModel> staticComponents;
    private final ImmutableList<Pair<Predicate<ModelState<?>>, FabricBakedModel>> dynamicComponents;

    public BakedStateModel(ModelState<?> defaultModelState, ImmutableList<FabricBakedModel> staticComponents, ImmutableList<Pair<Predicate<ModelState<?>>, FabricBakedModel>> dynamicComponents) {
        if (!staticComponents.isEmpty()) {
            this.masterModel = (BakedModel) staticComponents.get(0);
        } else if (!dynamicComponents.isEmpty()) {
            this.masterModel = (BakedModel) dynamicComponents.get(0);
        } else {
            throw new IllegalArgumentException("State Model should have at least one component");
        }

        this.defaultModelState = defaultModelState;
        this.staticComponents = staticComponents;
        this.dynamicComponents = dynamicComponents;
    }

    private void emitDynamicBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        ModelState<?> modelState = this.defaultModelState;

        if (state.getBlock() instanceof ModelStateProviderBlock providerBlock) {
            modelState = providerBlock.getModelState(blockView, state, pos);
        }

        for (Pair<Predicate<ModelState<?>>, FabricBakedModel> component : this.dynamicComponents) {
            if (component.getLeft().test(modelState)) {
                component.getRight().emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
        }
    }

    private void emitDynamicItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        ModelState<?> modelState = this.defaultModelState;

        if (stack.getItem() instanceof ModelStateProviderItem providerItem) {
            modelState = providerItem.getModelState(stack);
        } else if (Block.getBlockFromItem(stack.getItem()) instanceof ModelStateProviderBlock providerBlock) {
            modelState = providerBlock.getModelStateForItem(stack);
        }

        for (Pair<Predicate<ModelState<?>>, FabricBakedModel> component : this.dynamicComponents) {
            if (component.getLeft().test(modelState)) {
                component.getRight().emitItemQuads(stack, randomSupplier, context);
            }
        }
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        for (FabricBakedModel staticModel : this.staticComponents) {
            staticModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }

        if (!this.dynamicComponents.isEmpty()) {
            emitDynamicBlockQuads(blockView, state, pos, randomSupplier, context);
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        for (FabricBakedModel staticModel : this.staticComponents) {
            staticModel.emitItemQuads(stack, randomSupplier, context);
        }

        if (!this.dynamicComponents.isEmpty()) {
            emitDynamicItemQuads(stack, randomSupplier, context);
        }
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.masterModel.useAmbientOcclusion();
    }

    @Override
    public boolean hasDepth() {
        return this.masterModel.hasDepth();
    }

    @Override
    public boolean isSideLit() {
        return this.masterModel.isSideLit();
    }

    @Override
    public Sprite getSprite() {
        return this.masterModel.getSprite();
    }


    @Override
    public ModelTransformation getTransformation() {
        return this.masterModel.getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }
}
