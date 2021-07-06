package gregtech.api.gui.widgets;

import gregtech.api.gui.*;
import gregtech.api.gui.resources.SizedTextureArea;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.util.ClickData;
import gregtech.api.gui.util.GuiUtils;
import gregtech.api.gui.util.InputHelper;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Consumer;

public class ClickButtonWidget extends Widget {

    protected TextureArea buttonTexture = GuiTextures.VANILLA_BUTTON.getSubArea(0.0f, 0.0f, 1.0f, 0.5f);
    protected String displayText;
    protected int textColor = 0xFFFFFF;
    protected Consumer<ClickData> onPressCallback;

    public ClickButtonWidget(int xPosition, int yPosition, int width, int height, String displayText, Consumer<ClickData> onPressed) {
        super(new Position(xPosition, yPosition), new Size(width, height));
        this.displayText = displayText;
        this.onPressCallback = onPressed;
    }

    public ClickButtonWidget setButtonTexture(TextureArea buttonTexture) {
        this.buttonTexture = buttonTexture;
        return this;
    }

    public ClickButtonWidget setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    @Override
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        super.drawInBackground(matrices, mouseX, mouseY, deltaTicks, renderContext);
        Position position = getPosition();
        Size size = getSize();
        if (buttonTexture instanceof SizedTextureArea) {
            ((SizedTextureArea) buttonTexture).drawHorizontalCutSubArea(matrices, position.x, position.y, size.width, size.height, 0.0f, 1.0f);
        } else {
            buttonTexture.drawSubArea(matrices, position.x, position.y, size.width, size.height, 0.0f, 0.0f, 1.0f, 1.0f);
        }

        GuiUtils.drawString(matrices, I18n.translate(displayText),
                position.x + size.width / 2,
                position.y + size.height / 2, textColor, true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            triggerButton(button);
            return true;
        }
        return false;
    }

    protected void triggerButton(int button) {
        ClickData clickData = new ClickData(button, InputHelper.isShiftDown(), InputHelper.isCtrlDown());
        writeClientAction(1, clickData::writeToBuf);
        InputHelper.playButtonClickSound();
    }

    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            onPressCallback.accept(clickData);
        }
    }
}
