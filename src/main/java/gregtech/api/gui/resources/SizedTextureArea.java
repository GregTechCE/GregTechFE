package gregtech.api.gui.resources;

import gregtech.api.GTValues;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SizedTextureArea extends TextureArea {

    public final float pixelImageWidth;
    public final float pixelImageHeight;

    public SizedTextureArea(Identifier imageLocation, float offsetX, float offsetY, float width, float height, float pixelImageWidth, float pixelImageHeight) {
        super(imageLocation, offsetX, offsetY, width, height);
        this.pixelImageWidth = pixelImageWidth;
        this.pixelImageHeight = pixelImageHeight;
    }

    @Override
    public SizedTextureArea getSubArea(float offsetX, float offsetY, float width, float height) {
        return new SizedTextureArea(imageLocation,
            this.offsetX + (imageWidth * offsetX),
            this.offsetY + (imageHeight * offsetY),
            this.imageWidth * width,
            this.imageHeight * height,
            this.pixelImageWidth * width,
            this.pixelImageHeight * height);
    }

    public static SizedTextureArea fullImage(String imageLocation, int imageWidth, int imageHeight) {
        return new SizedTextureArea(new Identifier(GTValues.MODID, imageLocation), 0.0f, 0.0f, 1.0f, 1.0f, imageWidth, imageHeight);
    }

    public void drawHorizontalCutArea(MatrixStack matrices, int x, int y, int width, int height) {
        drawHorizontalCutSubArea(matrices, x, y, width, height, 0.0f, 1.0f);
    }

    public void drawVerticalCutArea(MatrixStack matrices, int x, int y, int width, int height) {
        drawVerticalCutSubArea(matrices, x, y, width, height, 0.0f, 1.0f);
    }

    public void drawHorizontalCutSubArea(MatrixStack matrices, int x, int y, int width, int height, float drawnV, float drawnHeight) {
        float drawnWidth = width / 2.0f / pixelImageWidth;
        drawSubArea(matrices, x, y, width / 2, height, 0.0f, drawnV, drawnWidth, drawnHeight);
        drawSubArea(matrices, x + width / 2.0f, y, width / 2, height, 1.0f - drawnWidth, drawnV, drawnWidth, drawnHeight);
    }

    public void drawVerticalCutSubArea(MatrixStack matrices, int x, int y, int width, int height, float drawnU, float drawnWidth) {
        float drawnHeight = height / 2.0f / pixelImageHeight;
        drawSubArea(matrices, x, y, width, height / 2, drawnU, 0.0f, drawnWidth, drawnHeight);
        drawSubArea(matrices, x, y + height / 2.0f, width, height / 2, drawnU, 1.0f - drawnHeight, drawnWidth, drawnHeight);
    }

}
