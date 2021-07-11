package gregtech.api.render;

import gregtech.api.items.util.AOEBreakItem;
import gregtech.mixin.accessor.WorldRendererAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ToolRenderHandler implements WorldRenderEvents.BeforeBlockOutline {

    public static final ToolRenderHandler INSTANCE = new ToolRenderHandler();
    private ToolRenderHandler() {}

    public void register() {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(this);
    }

    @Override
    public boolean beforeBlockOutline(WorldRenderContext context, @Nullable HitResult hitResult) {
        Int2ObjectMap<BlockBreakingInfo> blockBreakingInfo = ((WorldRendererAccessor) context.worldRenderer()).getBlockBreakingInfos();

        for (Int2ObjectMap.Entry<BlockBreakingInfo> entry : blockBreakingInfo.int2ObjectEntrySet()) {
            BlockBreakingInfo breakingInfo = entry.getValue();
            Entity entity = context.world().getEntityById(breakingInfo.getActorId());

            if (entity instanceof PlayerEntity playerEntity) {
                ItemStack mainHandStack = playerEntity.getMainHandStack();

                if (mainHandStack.getItem() instanceof AOEBreakItem item) {
                    List<BlockPos> multiBreakBlocks = item.getAOEBlocks(mainHandStack, playerEntity, hitResult);
                    BlockPos originalBlockPos = breakingInfo.getPos();
                    int breakingStage = breakingInfo.getStage();

                    renderBlockBreaking(context, multiBreakBlocks, originalBlockPos, breakingStage);
                }
            }
        }
        return true;
    }

    private void renderBlockBreaking(WorldRenderContext context, List<BlockPos> multiBreakBlocks, BlockPos excludePos, int currentStage) {
        Vec3d cameraPos = context.camera().getPos();
        MatrixStack matrixStack = context.matrixStack();
        ClientWorld world = context.world();

        BlockRenderManager blockRenderManager = context.gameRenderer().getClient().getBlockRenderManager();
        BufferBuilderStorage bufferBuilders = ((WorldRendererAccessor) context.worldRenderer()).getBufferBuilders();

        for (BlockPos blockPos : multiBreakBlocks) {
            if (blockPos.equals(excludePos)) {
                continue;
            }

            double cameraOffsetX = blockPos.getX() - cameraPos.getX();
            double cameraOffsetY = blockPos.getY() - cameraPos.getY();
            double cameraOffsetZ = blockPos.getZ() - cameraPos.getZ();

            double distanceSq = cameraOffsetX * cameraOffsetX + cameraOffsetY * cameraOffsetY + cameraOffsetZ * cameraOffsetZ;
            if (distanceSq <= 1024.0) {
                matrixStack.push();
                matrixStack.translate(cameraOffsetX, cameraOffsetY, cameraOffsetZ);

                MatrixStack.Entry matrixStackEntry = matrixStack.peek();

                RenderLayer blockBreakRenderLayer = ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(currentStage);
                VertexConsumer rawBlockBreakBuffer = bufferBuilders.getEffectVertexConsumers().getBuffer(blockBreakRenderLayer);

                VertexConsumer overlayVertexConsumer = new OverlayVertexConsumer(rawBlockBreakBuffer,
                        matrixStackEntry.getModel(),
                        matrixStackEntry.getNormal());

                BlockState blockState = world.getBlockState(blockPos);
                blockRenderManager.renderDamage(blockState, blockPos, world, matrixStack, overlayVertexConsumer);

                matrixStack.pop();
            }
        }
    }
}
