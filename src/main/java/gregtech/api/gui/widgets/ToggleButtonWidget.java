package gregtech.api.gui.widgets;

import com.google.common.base.Preconditions;
import gregtech.api.gui.*;
import gregtech.api.gui.resources.SizedTextureArea;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import gregtech.api.util.function.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public class ToggleButtonWidget extends Widget {

    protected TextureArea buttonTexture;
    private final BooleanSupplier isPressedCondition;
    private final BooleanConsumer setPressedExecutor;
    private String tooltipText;
    protected boolean isPressed;

    public ToggleButtonWidget(int xPosition, int yPosition, int width, int height, BooleanSupplier isPressedCondition, BooleanConsumer setPressedExecutor) {
        this(xPosition, yPosition, width, height, GuiTextures.VANILLA_BUTTON, isPressedCondition, setPressedExecutor);
    }

    public ToggleButtonWidget(int xPosition, int yPosition, int width, int height, TextureArea buttonTexture,
                              BooleanSupplier isPressedCondition, BooleanConsumer setPressedExecutor) {
        super(new Position(xPosition, yPosition), new Size(width, height));
        Preconditions.checkNotNull(buttonTexture, "texture");
        this.buttonTexture = buttonTexture;
        this.isPressedCondition = isPressedCondition;
        this.setPressedExecutor = setPressedExecutor;
    }

    public ToggleButtonWidget setButtonTexture(TextureArea texture) {
        Preconditions.checkNotNull(texture, "texture");
        this.buttonTexture = texture;
        return this;
    }

    public ToggleButtonWidget setTooltipText(String tooltipText) {
        Preconditions.checkNotNull(tooltipText, "tooltipText");
        this.tooltipText = tooltipText;
        return this;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, RenderContext context) {
        Position pos = getPosition();
        Size size = getSize();
        if (buttonTexture instanceof SizedTextureArea) {
            ((SizedTextureArea) buttonTexture).drawHorizontalCutSubArea(matrices, pos.x, pos.y, size.width, size.height, isPressed ? 0.5f : 0.0f, 0.5f);
        } else {
            buttonTexture.drawSubArea(matrices, pos.x, pos.y, size.width, size.height, 0.0f, isPressed ? 0.5f : 0.0f, 1.0f, 0.5f);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY) {
        if(isMouseOverElement(mouseX, mouseY) && tooltipText != null) {
            String postfix = isPressed ? ".enabled" : ".disabled";
            String tooltipHoverString = tooltipText + postfix;

            List<Text> hoverList = Arrays.stream(I18n.translate(tooltipHoverString).split("/n"))
                    .map(LiteralText::new)
                    .collect(Collectors.toList());
            GuiUtils.renderTooltip(matrices, hoverList, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (isPressedCondition.getAsBoolean() != isPressed) {
            this.isPressed = isPressedCondition.getAsBoolean();
            writeUpdateInfo(1, buf -> buf.writeBoolean(isPressed));
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == 1) {
            this.isPressed = buffer.readBoolean();
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (isMouseOverElement(mouseX, mouseY)) {
            this.isPressed = !this.isPressed;
            writeClientAction(1, buf -> buf.writeBoolean(isPressed));
            InputHelper.playButtonClickSound();
            return true;
        }
        return false;
    }


    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            this.isPressed = buffer.readBoolean();
            setPressedExecutor.apply(isPressed);
        }
    }
}
