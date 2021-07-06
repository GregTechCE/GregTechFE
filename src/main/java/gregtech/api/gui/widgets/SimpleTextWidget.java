package gregtech.api.gui.widgets;

import gregtech.api.gui.util.GuiUtils;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Supplier;

/**
 * Simple one-line text widget with text synced and displayed
 * as the raw string from the server
 */
public class SimpleTextWidget extends Widget {

    protected String formatLocale;
    protected int color;
    protected Supplier<String> textSupplier;
    protected String lastText = "";

    public SimpleTextWidget(int xPosition, int yPosition, String formatLocale, int color, Supplier<String> textSupplier) {
        super(new Position(xPosition, yPosition), Size.ZERO);
        this.color = color;
        this.formatLocale = formatLocale;
        this.textSupplier = textSupplier;
    }

    public SimpleTextWidget(int xPosition, int yPosition, String formatLocale, Supplier<String> textSupplier) {
        this(xPosition, yPosition, formatLocale, 0x404040, textSupplier);
    }

    private String getDisplayText() {
        return formatLocale.isEmpty() ? (I18n.hasTranslation(lastText) ? I18n.translate(lastText) : lastText) :
                I18n.translate(formatLocale, lastText);
    }

    private void updateSize() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int stringWidth = textRenderer.getWidth(getDisplayText());

        setSize(new Size(stringWidth, textRenderer.fontHeight));

        if (uiAccess != null) {
            uiAccess.notifySizeChange();
        }
    }

    @Override
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        String text = getDisplayText();
        Position position = getPosition();

        GuiUtils.drawString(matrices, text, position.x, position.y, color, true);
    }

    @Override
    public void detectAndSendChanges() {
        if (!textSupplier.get().equals(lastText)) {
            this.lastText = textSupplier.get();
            writeUpdateInfo(1, buffer -> buffer.writeString(lastText));
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        if (id == 1) {
            this.lastText = buffer.readString();
            updateSize();
        }
    }
}
