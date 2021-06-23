package gregtech.api.gui.resources;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class RenderUtil {

    public static void setGlColorFromInt(int colorValue, int opacity) {
        int i = (colorValue & 16711680) >> 16;
        int j = (colorValue & 65280) >> 8;
        int k = (colorValue & 255);
        RenderSystem.setShaderColor(i / 255.0f, j / 255.0f, k / 255.0f, opacity / 255.0f);
    }

    public static void setGlClearColorFromInt(int colorValue, int opacity) {
        int i = (colorValue & 16711680) >> 16;
        int j = (colorValue & 65280) >> 8;
        int k = (colorValue & 255);
        RenderSystem.clearColor(i / 255.0f, j / 255.0f, k / 255.0f, opacity / 255.0f);
    }

    public static int getFluidColor(FluidVolume fluidStack) {
        if (fluidStack.getFluidKey() == FluidKeys.WATER)
            return 0x3094CF;
        else if (fluidStack.getFluidKey() == FluidKeys.LAVA)
            return 0xFFD700;
        return fluidStack.getRenderColor();
    }

    @SuppressWarnings("deprecation")
    public static void drawFluidForGui(MatrixStack matrices, FluidVolume contents, FluidAmount tankCapacity, int startX, int startY, int widthT, int heightT) {
        widthT--;
        heightT--;

        MinecraftClient client = MinecraftClient.getInstance();
        Sprite fluidStillSprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(contents.getSprite());

        int fluidColor = contents.getRenderColor();
        int scaledAmount = (int) (contents.amount().div(tankCapacity).asInexactDouble() * heightT);

        if (!contents.isEmpty() && scaledAmount == 0) {
            scaledAmount = 1;
        }
        if (scaledAmount > heightT) {
            scaledAmount = heightT;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        setGlColorFromInt(fluidColor, 200);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

        final int xTileCount = widthT / 16;
        final int xRemainder = widthT - xTileCount * 16;
        final int yTileCount = scaledAmount / 16;
        final int yRemainder = scaledAmount - yTileCount * 16;

        final int yStart = startY + heightT;
        Matrix4f matrix = matrices.peek().getModel();

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = xTile == xTileCount ? xRemainder : 16;
                int height = yTile == yTileCount ? yRemainder : 16;
                int x = startX + xTile * 16;
                int y = yStart - (yTile + 1) * 16;
                if (width > 0 && height > 0) {
                    int maskTop = 16 - height;
                    int maskRight = 16 - width;

                    drawFluidTexture(matrix, x, y, fluidStillSprite, maskTop, maskRight, 0.0f);
                }
            }
        }

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    private static void drawFluidTexture(Matrix4f matrix, float xCoord, float yCoord, Sprite textureSprite, int maskTop, int maskRight, float zLevel) {
        float uMin = textureSprite.getMinU();
        float uMax = textureSprite.getMaxU();
        float vMin = textureSprite.getMinV();
        float vMax = textureSprite.getMaxV();
        uMax = uMax - maskRight / 16.0f * (uMax - uMin);
        vMax = vMax - maskTop / 16.0f * (vMax - vMin);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrix, xCoord, yCoord + 16, zLevel).texture(uMin, vMax).next();
        buffer.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).texture(uMax, vMax).next();
        buffer.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).texture(uMax, vMin).next();
        buffer.vertex(matrix, xCoord, yCoord + maskTop, zLevel).texture(uMin, vMin).next();
        
        tessellator.draw();
    }


}
