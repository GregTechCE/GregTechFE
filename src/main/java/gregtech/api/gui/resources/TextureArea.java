package gregtech.api.gui.resources;

import com.mojang.blaze3d.systems.RenderSystem;
import gregtech.api.GTValues;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

/**
 * Represents a texture area of image
 * This representation doesn't take image size in account, so all image variables are
 * 0.0 - 1.0 bounds
 */
public class TextureArea {

    public final Identifier imageLocation;

    public final float offsetX;
    public final float offsetY;

    public final float imageWidth;
    public final float imageHeight;

    public TextureArea(Identifier imageLocation, float offsetX, float offsetY, float width, float height) {
        this.imageLocation = imageLocation;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.imageWidth = width;
        this.imageHeight = height;
    }

    public static TextureArea fullImage(String imageLocation) {
        return new TextureArea(new Identifier(GTValues.MODID, imageLocation), 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public static TextureArea areaOfImage(String imageLocation, int imageSizeX, int imageSizeY, int u, int v, int width, int height) {
        return new TextureArea(new Identifier(imageLocation),
            u / (imageSizeX * 1.0f),
            v / (imageSizeY * 1.0f),
            (u + width) / (imageSizeX * 1.0f),
            (v + height) / (imageSizeY * 1.0f));
    }

    public TextureArea getSubArea(float offsetX, float offsetY, float width, float height) {
        return new TextureArea(imageLocation,
            this.offsetX + (imageWidth * offsetX),
            this.offsetY + (imageHeight * offsetY),
            this.imageWidth * width,
            this.imageHeight * height);
    }
    
    @Environment(EnvType.CLIENT)
    public void draw(MatrixStack matrices, float x, float y, int width, int height) {
        drawSubArea(matrices, x, y, width, height, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    @Environment(EnvType.CLIENT)
    public void drawSubArea(MatrixStack matrices, float x, float y, int width, int height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        float imageU = this.offsetX + (this.imageWidth * drawnU);
        float imageV = this.offsetY + (this.imageHeight * drawnV);
        float imageWidth = this.imageWidth * drawnWidth;
        float imageHeight = this.imageHeight * drawnHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.imageLocation);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        Matrix4f matrix = matrices.peek().getModel();
        
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        builder.vertex(matrix, x, y + height, 0.0f).texture(imageU, imageV + imageHeight).next();
        builder.vertex(matrix, x + width, y + height, 0.0f).texture(imageU + imageWidth, imageV + imageHeight).next();
        builder.vertex(matrix, x + width, y, 0.0f).texture(imageU + imageWidth, imageV).next();
        builder.vertex(matrix, x, y, 0.0f).texture(imageU, imageV).next();
        tessellator.draw();
    }
}
