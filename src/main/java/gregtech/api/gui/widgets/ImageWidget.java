package gregtech.api.gui.widgets;

import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;

import java.util.function.BooleanSupplier;

public class ImageWidget extends Widget {

    protected final TextureArea area;
    private BooleanSupplier predicate;
    private boolean isVisible = true;

    public ImageWidget(int xPosition, int yPosition, int width, int height, TextureArea area) {
        super(new Position(xPosition, yPosition), new Size(width, height));
        this.area = area;
    }

    public ImageWidget setPredicate(BooleanSupplier predicate) {
        this.predicate = predicate;
        this.isVisible = false;
        return this;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (predicate != null && predicate.getAsBoolean() != isVisible) {
            this.isVisible = predicate.getAsBoolean();
            writeUpdateInfo(1, buf -> buf.writeBoolean(isVisible));
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == 1) {
            this.isVisible = buffer.readBoolean();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, RenderContext context) {
        if (!this.isVisible || area == null) return;
        Position position = getPosition();
        Size size = getSize();
        area.draw(matrices, position.x, position.y, size.width, size.height);
    }

}

