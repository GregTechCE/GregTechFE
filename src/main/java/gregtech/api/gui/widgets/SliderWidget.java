package gregtech.api.gui.widgets;

import com.google.common.base.Preconditions;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.GuiUtils;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import gregtech.api.util.function.FloatConsumer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

import java.util.function.BiFunction;

public class SliderWidget extends Widget {

    public static final BiFunction<String, Double, String> DEFAULT_TEXT_SUPPLIER = (name, value) -> I18n.translate(name, value.intValue());

    private int sliderWidth = 8;
    private TextureArea backgroundArea = GuiTextures.SLIDER_BACKGROUND;
    private TextureArea sliderIcon = GuiTextures.SLIDER_ICON;
    private final BiFunction<String, Double, String> textSupplier = DEFAULT_TEXT_SUPPLIER;
    private int textColor = 0xFFFFFF;

    private final float min;
    private final float max;
    private final String name;

    private final FloatConsumer responder;
    private boolean isPositionSent;

    private String displayString;
    private double sliderPosition;
    public boolean isMouseDown;

    public SliderWidget(String name, int xPosition, int yPosition, int width, int height, float min, float max, float currentValue, FloatConsumer responder) {
        super(new Position(xPosition, yPosition), new Size(width, height));
        this.min = min;
        this.max = max;
        this.name = name;
        this.responder = responder;
        this.sliderPosition = (currentValue - min) / (max - min);
    }

    public SliderWidget setSliderIcon(TextureArea sliderIcon) {
        Preconditions.checkNotNull(sliderIcon, "sliderIcon");
        this.sliderIcon = sliderIcon;
        return this;
    }

    public SliderWidget setBackground(TextureArea background) {
        this.backgroundArea = background;
        return this;
    }

    public SliderWidget setSliderWidth(int sliderWidth) {
        this.sliderWidth = sliderWidth;
        return this;
    }

    public SliderWidget setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    @Override
    public void detectAndSendChanges() {
        if (!isPositionSent) {
            writeUpdateInfo(1, buffer -> buffer.writeDouble(sliderPosition));
            this.isPositionSent = true;
        }
    }

    public double getSliderValue() {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    protected String getDisplayString() {
        return textSupplier.apply(name, getSliderValue());
    }

    @Override
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        Position pos = getPosition();
        Size size = getSize();
        if (backgroundArea != null) {
            backgroundArea.draw(matrices, pos.x, pos.y, size.width, size.height);
        }
        if (displayString == null) {
            this.displayString = getDisplayString();
        }

        sliderIcon.draw(matrices, pos.x + (int) (this.sliderPosition * (float) (size.width - 8)), pos.y, sliderWidth, size.height);
        GuiUtils.drawString(matrices, displayString,
                pos.x + size.width / 2,
                pos.y + size.height / 2,
                textColor, true);
    }

    private void handleMouseMovement(int mouseX, int mouseY) {
        Position pos = getPosition();
        Size size = getSize();
        this.sliderPosition = (float) (mouseX - (pos.x + 4)) / (float) (size.width - 8);

        if (this.sliderPosition < 0.0F) {
            this.sliderPosition = 0.0F;
        }

        if (this.sliderPosition > 1.0F) {
            this.sliderPosition = 1.0F;
        }
        this.displayString = this.getDisplayString();
        writeClientAction(1, buffer -> buffer.writeDouble(sliderPosition));
        this.isMouseDown = true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isMouseDown) {
            handleMouseMovement(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            handleMouseMovement(mouseX, mouseY);
            this.isMouseDown = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isMouseDown = false;
        return false;
    }

    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        if (id == 1) {
            this.sliderPosition = buffer.readDouble();
            this.sliderPosition = MathHelper.clamp(sliderPosition, 0.0f, 1.0f);
            this.responder.apply((float) getSliderValue());
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        if (id == 1) {
            this.sliderPosition = buffer.readDouble();
            this.sliderPosition = MathHelper.clamp(sliderPosition, 0.0f, 1.0f);
            this.displayString = getDisplayString();
        }
    }
}
