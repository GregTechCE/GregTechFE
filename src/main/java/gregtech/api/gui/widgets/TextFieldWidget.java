package gregtech.api.gui.widgets;

import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import gregtech.mixin.accessor.ClickableWidgetAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TextFieldWidget extends Widget {

    @Environment(EnvType.CLIENT)
    protected net.minecraft.client.gui.widget.TextFieldWidget textField;

    protected int maxStringLength = 32;
    protected Predicate<String> textValidator;
    protected final Supplier<String> textSupplier;
    protected final Consumer<String> textResponder;
    protected String currentString;

    public TextFieldWidget(int xPosition, int yPosition, int width, int height, Supplier<String> textSupplier, Consumer<String> textResponder) {
        super(new Position(xPosition, yPosition), new Size(width, height));
        this.textSupplier = textSupplier;
        this.textResponder = textResponder;

        if (isClientSide()) {
            initClientWidget();
        }
    }

    @Environment(EnvType.CLIENT)
    private void initClientWidget() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Position position = getPosition();
        Size size = getSize();

        Text narratorText = new LiteralText("");
        this.textField = new net.minecraft.client.gui.widget.TextFieldWidget(textRenderer, position.x, position.y, size.width, size.height, narratorText);

        this.textField.setMaxLength(maxStringLength);
        this.textField.setTextPredicate(this.textValidator);
        this.textField.setChangedListener(this::onTextChanged);
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected void onPositionUpdate() {
        Position position = getPosition();
        this.textField.setX(position.x);
        ((ClickableWidgetAccessor) this.textField).setY(position.y);
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected void onSizeUpdate() {
        Size size = getSize();
        this.textField.setWidth(size.width);
        ((ClickableWidgetAccessor) this.textField).setHeight(size.height);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void updateScreen() {
        this.textField.tick();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.textField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.textField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean charTyped(char chr, int modifiers) {
        return this.textField.charTyped(chr, modifiers);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        this.textField.renderButton(matrices, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!textSupplier.get().equals(currentString)) {
            this.currentString = textSupplier.get();
            writeUpdateInfo(1, buffer -> buffer.writeString(currentString));
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == 1) {
            this.currentString = buffer.readString(Short.MAX_VALUE);
            this.textField.setText(currentString);
        }
    }

    protected void onTextChanged(String newTextString) {
        if (textValidator.test(newTextString)) {
            writeClientAction(1, buffer -> buffer.writeString(newTextString));
        }
    }

    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            String clientText = buffer.readString();
            clientText = clientText.substring(0, Math.min(clientText.length(), maxStringLength));

            if (textValidator.test(clientText)) {
                this.currentString = clientText;
                this.textResponder.accept(clientText);
            }
        }
    }

    public TextFieldWidget setTextColor(int editableTextColor, int uneditableTextColor) {
        if (isClientSide()) {
            this.textField.setEditableColor(editableTextColor);
            this.textField.setUneditableColor(uneditableTextColor);
        }
        return this;
    }

    public TextFieldWidget setValidator(Predicate<String> validator) {
        this.textValidator = validator;
        if (isClientSide()) {
            this.textField.setTextPredicate(validator);
        }
        return this;
    }

    public TextFieldWidget setMaxStringLength(int maxStringLength) {
        this.maxStringLength = maxStringLength;
        if (isClientSide()) {
            this.textField.setMaxLength(maxStringLength);
        }
        return this;
    }

    public TextFieldWidget setBackgroundDrawn(boolean drawBackground) {
        if (isClientSide()) {
            this.textField.setDrawsBackground(drawBackground);
        }
        return this;
    }
}
