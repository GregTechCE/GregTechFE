package gregtech.api.gui.widgets;

import gregtech.api.gui.*;
import gregtech.api.gui.util.ClickData;
import gregtech.api.gui.util.GuiUtils;
import gregtech.api.gui.util.InputHelper;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Represents a text-component based widget, which obtains
 * text from server and automatically synchronizes it with clients
 */
public class AdvancedTextWidget extends Widget {
    protected int maxWidthLimit;

    protected BiConsumer<String, ClickData> clickHandler;
    protected Consumer<List<Text>> textSupplier;
    private List<Text> cachedText = Collections.emptyList();
    private List<OrderedText> displayText = Collections.emptyList();

    private final int color;

    public AdvancedTextWidget(int xPosition, int yPosition, Consumer<List<Text>> text, int color) {
        super(new Position(xPosition, yPosition), Size.ZERO);
        this.textSupplier = text;
        this.color = color;
    }

    public static MutableText withButton(MutableText textComponent, String componentData) {
        textComponent.setStyle(textComponent.getStyle()
            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "@!" + componentData))
            .withColor(Formatting.YELLOW));
        return textComponent;
    }

    public static MutableText withHoverTextTranslate(MutableText textComponent, String hoverTranslation) {
        MutableText translatedHover = new TranslatableText(hoverTranslation);
        translatedHover.setStyle(translatedHover.getStyle()
            .withColor(Formatting.GRAY));

        textComponent.setStyle(textComponent.getStyle()
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, translatedHover)));
        return textComponent;
    }

    public AdvancedTextWidget setMaxWidthLimit(int maxWidthLimit) {
        this.maxWidthLimit = maxWidthLimit;
        if (isClientSide()) {
            updateComponentTextSize();
        }
        return this;
    }

    public AdvancedTextWidget setClickHandler(BiConsumer<String, ClickData> clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    @Override
    public void detectAndSendChanges() {
        ArrayList<Text> textBuffer = new ArrayList<>();
        this.textSupplier.accept(textBuffer);

        if (!cachedText.equals(textBuffer)) {
            this.cachedText = textBuffer;

            writeUpdateInfo(1, buffer -> {
                buffer.writeVarInt(cachedText.size());
                for (Text textComponent : cachedText) {
                    buffer.writeString(Text.Serializer.toJson(textComponent));
                }
            });
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        if (id == 1) {
            this.cachedText.clear();
            int count = buffer.readVarInt();

            for (int i = 0; i < count; i++) {
                String jsonText = buffer.readString();
                this.cachedText.add(Text.Serializer.fromJson(jsonText));
            }
            refreshDisplayText();
            updateComponentTextSize();
        }
    }

    @Environment(EnvType.CLIENT)
    protected Style getTextUnderMouse(int mouseX, int mouseY) {
        TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;
        TextHandler textHandler = fontRenderer.getTextHandler();

        Position position = getPosition();
        int selectedLine = (mouseY - position.y) / (fontRenderer.fontHeight + 2);

        if (mouseX >= position.x && selectedLine >= 0 && selectedLine < displayText.size()) {

            OrderedText selectedComponent = displayText.get(selectedLine);
            int mouseOffset = mouseX - position.x;
            return textHandler.getStyleAt(selectedComponent, mouseOffset);
        }
        return null;
    }

    @Environment(EnvType.CLIENT)
    private void updateComponentTextSize() {
        TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;
        TextHandler textHandler = fontRenderer.getTextHandler();

        int totalHeight = this.displayText.size() * (fontRenderer.fontHeight + 2);
        if (!displayText.isEmpty()) {
            totalHeight -= 2;
        }

        int maximumWidth = (int) this.displayText.stream()
                .mapToDouble(textHandler::getWidth)
                .max().orElse(0);

        setSize(new Size(maximumWidth, totalHeight));
        if (uiAccess != null) {
            uiAccess.notifySizeChange();
        }
    }

    @Environment(EnvType.CLIENT)
    private void refreshDisplayText() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int maxTextWidthResult = maxWidthLimit == 0 ? Integer.MAX_VALUE : maxWidthLimit;

        this.displayText = cachedText.stream()
            .flatMap(c -> ChatMessages.breakRenderedChatMessageLines(c, maxTextWidthResult, textRenderer).stream())
            .collect(Collectors.toList());
    }

    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            String componentData = buffer.readString(128);
            if (clickHandler != null) {
                clickHandler.accept(componentData, clickData);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private boolean handleCustomComponentClick(Style style) {
        if (style.getClickEvent() != null) {
            ClickEvent clickEvent = style.getClickEvent();
            String componentText = clickEvent.getValue();

            if (clickEvent.getAction() == ClickEvent.Action.OPEN_URL && componentText.startsWith("@!")) {
                String rawText = componentText.substring(2);
                ClickData clickData = new ClickData(InputHelper.getActiveMouseButton(),
                        InputHelper.isShiftDown(), InputHelper.isCtrlDown());

                writeClientAction(1, buf -> {
                    clickData.writeToBuf(buf);
                    buf.writeString(rawText);
                });
                return true;
            }
        }
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Style textComponent = getTextUnderMouse((int) mouseX, (int) mouseY);
        if (textComponent != null) {
            if (handleCustomComponentClick(textComponent) ||
                GuiUtils.handleTextClick(textComponent, null)) {
                InputHelper.playButtonClickSound();
                return true;
            }
        }
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;
        Position position = getPosition();

        for (int i = 0; i < displayText.size(); i++) {
            int yPosition = position.y + i * (fontRenderer.fontHeight + 2);
            fontRenderer.draw(matrices, displayText.get(i), position.x, yPosition, color);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY, RenderContext renderContext) {
        Style hoveredStyle = getTextUnderMouse(mouseX, mouseY);

        if (hoveredStyle != null && hoveredStyle.getHoverEvent() != null) {
            GuiUtils.drawHoverEvent(matrices, mouseX, mouseY, hoveredStyle.getHoverEvent());
        }
    }
}
