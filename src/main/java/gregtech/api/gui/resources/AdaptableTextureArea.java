package gregtech.api.gui.resources;

import gregtech.api.GTValues;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AdaptableTextureArea extends SizedTextureArea {

    private final int pixelCornerWidth;
    private final int pixelCornerHeight;

    public AdaptableTextureArea(Identifier imageLocation, float offsetX, float offsetY, float width, float height, float pixelImageWidth, float pixelImageHeight, int pixelCornerWidth, int pixelCornerHeight) {
        super(imageLocation, offsetX, offsetY, width, height, pixelImageWidth, pixelImageHeight);
        this.pixelCornerWidth = pixelCornerWidth;
        this.pixelCornerHeight = pixelCornerHeight;
    }

    public static AdaptableTextureArea fullImage(String imageLocation, int imageWidth, int imageHeight, int cornerWidth, int cornerHeight) {
        return new AdaptableTextureArea(new Identifier(GTValues.MODID, imageLocation), 0.0f, 0.0f, 1.0f, 1.0f, imageWidth, imageHeight, cornerWidth, cornerHeight);
    }

    @Override
    public void drawSubArea(MatrixStack matrices, float x, float y, int width, int height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        //compute relative sizes
        float cornerWidth = pixelCornerWidth / pixelImageWidth;
        float cornerHeight = pixelCornerHeight / pixelImageHeight;
        //draw up corners
        super.drawSubArea(matrices, x, y, pixelCornerWidth, pixelCornerHeight, 0.0f, 0.0f, cornerWidth, cornerHeight);
        super.drawSubArea(matrices, x + width - pixelCornerWidth, y, pixelCornerWidth, pixelCornerHeight, 1.0f - cornerWidth, 0.0f, cornerWidth, cornerHeight);
        //draw down corners
        super.drawSubArea(matrices, x, y + height - pixelCornerHeight, pixelCornerWidth, pixelCornerHeight, 0.0f, 1.0f - cornerHeight, cornerWidth, cornerHeight);
        super.drawSubArea(matrices, x + width - pixelCornerWidth, y + height - pixelCornerHeight, pixelCornerWidth, pixelCornerHeight, 1.0f - cornerWidth, 1.0f - cornerHeight, cornerWidth, cornerHeight);
        //draw horizontal connections
        super.drawSubArea(matrices, x + pixelCornerWidth, y, width - 2 * pixelCornerWidth, pixelCornerHeight,
            cornerWidth, 0.0f, 1.0f - 2 * cornerWidth, cornerHeight);
        super.drawSubArea(matrices, x + pixelCornerWidth, y + height - pixelCornerHeight, width - 2 * pixelCornerWidth, pixelCornerHeight,
             cornerWidth, 1.0f - cornerHeight, 1.0f - 2 * cornerWidth, cornerHeight);
        //draw vertical connections
        super.drawSubArea(matrices, x, y + pixelCornerHeight, pixelCornerWidth, height - 2 * pixelCornerHeight,
            0.0f, cornerHeight, cornerWidth, 1.0f - 2 * cornerHeight);
        super.drawSubArea(matrices, x + width - pixelCornerWidth, y + pixelCornerHeight, pixelCornerWidth, height - 2 * pixelCornerHeight,
            1.0f - cornerWidth, cornerHeight, cornerWidth, 1.0f - 2 * cornerHeight);
        //draw central body
        super.drawSubArea(matrices, x + pixelCornerWidth, y + pixelCornerHeight,
            width - 2 * pixelCornerWidth, height - 2 * pixelCornerHeight,
            cornerWidth, cornerHeight, 1.0f - 2 * cornerWidth, 1.0f - 2 * cornerHeight);
    }
}
