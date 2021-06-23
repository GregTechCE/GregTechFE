package gregtech.api.gui.resources;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public final class EmptyTextureArea extends TextureArea {

    public static final EmptyTextureArea INSTANCE = new EmptyTextureArea();

    private EmptyTextureArea() {
        super(new Identifier("missingno"), 0.0f, 0.0f, 1.0f, 1.0f);
    }

    @Override
    public TextureArea getSubArea(float offsetX, float offsetY, float width, float height) {
        return this;
    }

    @Override
    public void draw(MatrixStack matrices, float x, float y, int width, int height) {
    }

    @Override
    public void drawSubArea(MatrixStack matrices, float x, float y, int width, int height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
    }
}
