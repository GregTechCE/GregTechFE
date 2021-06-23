package gregtech.api.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class GuiUtils {

    //Packed int 0xAARRGGBB -> float [r, g, b, a]
    public static float[] unpackColor(int color) {
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        return new float[] {r, g, b, a};
    }

    public static void drawString(MatrixStack matrices, String text, int x, int y, int color, boolean center) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int xOffset = center ? (-textRenderer.getWidth(text) / 2) : 0;
        int yOffset = center ? (-textRenderer.fontHeight / 2) : 0;
        textRenderer.draw(matrices, text, x + xOffset, y + yOffset, color);
    }

    public static void renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y) {
        renderTooltip(matrices, getItemToolTip(stack), stack.getTooltipData(), x, y);
    }

    public static void renderTooltip(MatrixStack matrices, List<Text> lines, Optional<TooltipData> data, int x, int y) {
        List<TooltipComponent> tooltip = lines.stream()
                .map(Text::asOrderedText)
                .map(TooltipComponent::of)
                .collect(Collectors.toList());
        data.ifPresent(dataObject -> tooltip.add(1, TooltipComponent.of(dataObject)));

        renderTooltipFromComponents(matrices, tooltip, x, y);
    }

    public static void renderTooltipFromComponents(MatrixStack matrices, List<TooltipComponent> components, int x, int y) {
        if (components.isEmpty()) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        ItemRenderer itemRenderer = client.getItemRenderer();

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int maximumWidth = 0;
        int totalHeight = components.size() == 1 ? -2 : 0;

        for (TooltipComponent component : components) {
            totalHeight += component.getHeight();
            maximumWidth = Math.max(maximumWidth, component.getWidth(textRenderer));
        }

        int textStartX = x + 12;
        int gradientStartY = y - 12;
        if (textStartX + maximumWidth > screenWidth) {
            textStartX -= 28 + maximumWidth;
        }
        if (gradientStartY + totalHeight + 6 > screenHeight) {
            gradientStartY = screenHeight - totalHeight - 6;
        }

        matrices.push();
        float oldZOffset = itemRenderer.zOffset;
        itemRenderer.zOffset = 400.0f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f matrix = matrices.peek().getModel();

        fillGradient(matrix, builder, textStartX - 3, gradientStartY - 4, textStartX + maximumWidth + 3, gradientStartY - 3, 400, 0xF0100010, 0xF0100010);
        fillGradient(matrix, builder, textStartX - 3, gradientStartY + totalHeight + 3, textStartX + maximumWidth + 3, gradientStartY + totalHeight + 4, 400, 0xF0100010, 0xF0100010);
        fillGradient(matrix, builder, textStartX - 3, gradientStartY - 3, textStartX + maximumWidth + 3, gradientStartY + totalHeight + 3, 400, 0xF0100010, 0xF0100010);
        fillGradient(matrix, builder, textStartX - 4, gradientStartY - 3, textStartX - 3, gradientStartY + totalHeight + 3, 400, 0xF0100010, 0xF0100010);
        fillGradient(matrix, builder, textStartX + maximumWidth + 3, gradientStartY - 3, textStartX + maximumWidth + 4, gradientStartY + totalHeight + 3, 400, 0xF0100010, 0xF0100010);
        fillGradient(matrix, builder, textStartX - 3, gradientStartY - 3 + 1, textStartX - 3 + 1, gradientStartY + totalHeight + 3 - 1, 400, 0x505000FF, 0x5028007F);
        fillGradient(matrix, builder, textStartX + maximumWidth + 2, gradientStartY - 3 + 1, textStartX + maximumWidth + 3, gradientStartY + totalHeight + 3 - 1, 400, 0x505000FF, 0x5028007F);
        fillGradient(matrix, builder, textStartX - 3, gradientStartY - 3, textStartX + maximumWidth + 3, gradientStartY - 3 + 1, 400, 0x505000FF, 0x505000FF);
        fillGradient(matrix, builder, textStartX - 3, gradientStartY + totalHeight + 2, textStartX + maximumWidth + 3, gradientStartY + totalHeight + 3, 400, 0x5028007F, 0x5028007F);

        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        builder.end();
        BufferRenderer.draw(builder);

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        matrices.translate(0.0, 0.0, 400.0);
        int currentTextY = gradientStartY;

        for (int i = 0; i < components.size(); i++) {
            TooltipComponent component = components.get(i);
            component.drawText(textRenderer, textStartX, currentTextY, matrix, immediate);
            currentTextY += component.getHeight() + (i == 0 ? 2 : 0);
        }

        immediate.draw();
        matrices.pop();

        currentTextY = gradientStartY;
        for (int i = 0; i < components.size(); i++) {
            TooltipComponent component = components.get(i);
            component.drawItems(textRenderer, textStartX, currentTextY, matrices, itemRenderer, 400, client.getTextureManager());
            currentTextY += component.getHeight() + (i == 0 ? 2 : 0);
        }

        itemRenderer.zOffset = oldZOffset;
    }

    public static List<Text> getItemToolTip(ItemStack itemStack) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        boolean advancedTooltips = client.options.advancedItemTooltips;
        return itemStack.getTooltip(player, advancedTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.NORMAL);
    }

    public static void drawItemStack(ItemStack itemStack, int x, int y, @Nullable String altTxt) {
        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = client.getItemRenderer();
        TextRenderer textRenderer = client.textRenderer;

        itemRenderer.zOffset = 200.0F;
        itemRenderer.renderInGuiWithOverrides(itemStack, x, y);
        itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, x, y, altTxt);
        itemRenderer.zOffset = 0.0F;
    }

    public static void drawSolidRect(MatrixStack matrices, int x1, int y1, int x2, int y2, int color, int z) {
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        fillSolidRect(matrices.peek().getModel(), builder, x1, y1, x2, y2, color, z);
        tessellator.draw();
        
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void fillSolidRect(Matrix4f matrix, BufferBuilder builder, int x1, int y1, int x2, int y2, int color, int z) {
        float[] c = unpackColor(color);

        builder.vertex(matrix, x1, y2, z).color(c[0], c[1], c[2], c[3]).next();
        builder.vertex(matrix, x2, y2, z).color(c[0], c[1], c[2], c[3]).next();
        builder.vertex(matrix, x2, y1, z).color(c[0], c[1], c[2], c[3]).next();
        builder.vertex(matrix, x1, y1, z).color(c[0], c[1], c[2], c[3]).next();
    }

    public static void drawSelectionBox(MatrixStack matrices, int x, int y, int width, int height, int z) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        drawGradientRect(matrices, x, y, x + width, y + height, 0x7F000000, 0x7F000000, z);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }

    public static void drawGradientRect(MatrixStack matrices, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        fillGradient(matrices.peek().getModel(), builder, startX, startY, endX, endY, z, colorStart, colorEnd);
        tessellator.draw();

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void fillGradient(Matrix4f matrix, BufferBuilder builder, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
        float[] c1 = unpackColor(colorStart);
        float[] c2 = unpackColor(colorEnd);

        builder.vertex(matrix, endX, startY, z).color(c1[0], c1[1], c1[2], c1[3]).next();
        builder.vertex(matrix, startX, startY, z).color(c1[0], c1[1], c1[2], c1[3]).next();
        builder.vertex(matrix, startX, endY, z).color(c2[0], c2[1], c2[2], c2[3]).next();
        builder.vertex(matrix, endX, endY, z).color(c2[0], c2[1], c2[2], c2[3]).next();
    }
}
