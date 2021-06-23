package gregtech.api.gui.widgets;

import gregtech.api.gui.GuiUtils;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;

public class LabelWidget extends Widget {

    protected boolean centered = false;
    protected String text;
    protected Object[] formatting;
    private final int color;

    public LabelWidget(int xPosition, int yPosition, String text, Object... formatting) {
        this(xPosition, yPosition, text, 0x404040, formatting);
    }

    public LabelWidget(int xPosition, int yPosition, String text, int color, Object... formatting) {
        super(new Position(xPosition, yPosition), Size.ZERO);
        this.text = text;
        this.color = color;
        this.formatting = formatting;
        recomputeSize();
    }

    @Environment(EnvType.CLIENT)
    private String getResultText() {
        return I18n.translate(text, formatting);
    }

    private void recomputeSize() {
        if (isClientSide()) {
            String resultText = getResultText();
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            setSize(new Size(textRenderer.getWidth(resultText), textRenderer.fontHeight));

            if (uiAccess != null) {
                uiAccess.notifySizeChange();
            }
        }
    }

    public LabelWidget setCentered() {
        this.centered = true;
        return this;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, RenderContext context) {
        String resultText = getResultText();
        Position pos = getPosition();

        GuiUtils.drawString(matrices, resultText, pos.x, pos.y, color, centered);
    }

}
