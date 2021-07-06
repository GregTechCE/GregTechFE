package gregtech.api.gui.widgets;

import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;

import java.util.function.DoubleSupplier;

public class ProgressWidget extends Widget {

    private static final double EPSILON = 0.005;

    public enum MoveType {
        VERTICAL,
        HORIZONTAL,
        VERTICAL_INVERTED
    }

    public final DoubleSupplier progressSupplier;
    private MoveType moveType;
    private TextureArea emptyBarArea;
    private TextureArea filledBarArea;

    private double lastProgressValue;

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
    }

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height, TextureArea fullImage, MoveType moveType) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
        this.emptyBarArea = fullImage.getSubArea(0.0f, 0.0f, 1.0f, 0.5f);
        this.filledBarArea = fullImage.getSubArea(0.0f, 0.5f, 1.0f, 0.5f);
        this.moveType = moveType;
    }

    public ProgressWidget setProgressBar(TextureArea emptyBarArea, TextureArea filledBarArea, MoveType moveType) {
        this.emptyBarArea = emptyBarArea;
        this.filledBarArea = filledBarArea;
        this.moveType = moveType;
        return this;
    }

    @Override
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        Position pos = getPosition();
        Size size = getSize();
        if (emptyBarArea != null) {
            emptyBarArea.draw(matrices, pos.x, pos.y, size.width, size.height);
        }
        if (filledBarArea != null) {
            //fuck this precision-dependent things, they are so fucking annoying
            if (moveType == MoveType.HORIZONTAL) {
                filledBarArea.drawSubArea(matrices, pos.x, pos.y, (int) (size.width * lastProgressValue), size.height,
                    0.0f, 0.0f, ((int) (size.width * lastProgressValue)) / (size.width * 1.0f), 1.0f);

            } else if (moveType == MoveType.VERTICAL) {
                int progressValueScaled = (int) (size.height * lastProgressValue);
                filledBarArea.drawSubArea(matrices, pos.x, pos.y + size.height - progressValueScaled, size.width, progressValueScaled,
                    0.0f, 1.0f - (progressValueScaled / (size.height * 1.0f)),
                    1.0f, (progressValueScaled / (size.height * 1.0f)));

            } else if (moveType == MoveType.VERTICAL_INVERTED) {
                int progressValueScaled = (int) (size.height * lastProgressValue);
                filledBarArea.drawSubArea(matrices, pos.x, pos.y, size.width, progressValueScaled,
                    0.0f, 0.0f,
                    1.0f, (progressValueScaled / (size.height * 1.0f)));
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        double actualValue = progressSupplier.getAsDouble();
        if (Math.abs(actualValue - lastProgressValue) > EPSILON) {
            this.lastProgressValue = actualValue;
            writeUpdateInfo(0, buffer -> buffer.writeDouble(actualValue));
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        if (id == 0) {
            this.lastProgressValue = buffer.readDouble();
        }
    }
}
