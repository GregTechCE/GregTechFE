package gregtech.api.gui.widgets;

import gregtech.api.gui.util.GuiUtils;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Represents a label with text, dynamically obtained
 * from supplied getter in constructor
 * Note that this DOESN'T DO SYNC and calls getter on client side only
 * if you're looking for server-side controlled text field, see {@link gregtech.api.gui.widgets.AdvancedTextWidget}
 */
public class DynamicLabelWidget extends Widget {

    protected Supplier<String> textSupplier;
    private String lastTextValue = "";
    private final int color;

    public DynamicLabelWidget(int xPosition, int yPosition, Supplier<String> text) {
        this(xPosition, yPosition, text, 0x404040);
    }

    public DynamicLabelWidget(int xPosition, int yPosition, Supplier<String> text, int color) {
        super(new Position(xPosition, yPosition), Size.ZERO);
        this.textSupplier = text;
        this.color = color;
    }

    @Environment(EnvType.CLIENT)
    private void updateSize() {
        TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;

        int maximumWidth = Arrays.stream(lastTextValue.split("\n"))
                .mapToInt(fontRenderer::getWidth)
                .max().orElse(0);
        setSize(new Size(maximumWidth, fontRenderer.fontHeight));

        if (uiAccess != null) {
            uiAccess.notifySizeChange();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY, RenderContext renderContext) {
        String suppliedText = textSupplier.get();

        if (!suppliedText.equals(lastTextValue)) {
            this.lastTextValue = suppliedText;
            updateSize();
        }

        String[] split = textSupplier.get().split("\n");
        TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;
        Position position = getPosition();

        for (int i = 0; i < split.length; i++) {
            GuiUtils.drawString(matrices, split[i], position.x, position.y + (i * (fontRenderer.fontHeight + 2)), color, false);
        }
    }

}
