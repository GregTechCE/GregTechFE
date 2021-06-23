package gregtech.api.gui.widgets;

import gregtech.api.gui.ClickData;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
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

    @Environment(EnvType.CLIENT)
    private WrapScreen wrapScreen;

    protected Consumer<List<Text>> textSupplier;
    protected BiConsumer<String, ClickData> clickHandler;
    private List<Text> displayText = new ArrayList<>();
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

    @Environment(EnvType.CLIENT)
    private WrapScreen getWrapScreen() {
        if (wrapScreen == null) {
            wrapScreen = new WrapScreen();
        }
        return wrapScreen;
    }

    @Environment(EnvType.CLIENT)
    private void resizeWrapScreen() {
        if (sizes != null) {
            getWrapScreen().(Minecraft.getMinecraft(), sizes.getScreenWidth(), sizes.getScreenHeight());
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        if (isClientSide()) {
            resizeWrapScreen();
        }
    }

    @Override
    protected void onPositionUpdate() {
        super.onPositionUpdate();
        if (isClientSide()) {
            resizeWrapScreen();
        }
    }

    @Override
    public void detectAndSendChanges() {
        ArrayList<Text> textBuffer = new ArrayList<>();
        textSupplier.accept(textBuffer);

        if (!displayText.equals(textBuffer)) {
            this.displayText = textBuffer;

            writeUpdateInfo(1, buffer -> {
                buffer.writeVarInt(displayText.size());
                for (Text textComponent : displayText) {
                    buffer.writeString(Text.Serializer.toJson(textComponent));
                }
            });
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        if (id == 1) {
            this.displayText.clear();
            int count = buffer.readVarInt();

            for (int i = 0; i < count; i++) {
                String jsonText = buffer.readString();
                this.displayText.add(Text.Serializer.fromJson(jsonText));
            }
            formatDisplayText();
            updateComponentTextSize();
        }
    }

    protected ITextComponent getTextUnderMouse(int mouseX, int mouseY) {
        TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;

        Position position = getPosition();
        int selectedLine = (mouseY - position.y) / (fontRenderer.fontHeight + 2);

        if (mouseX >= position.x && selectedLine >= 0 && selectedLine < displayText.size()) {
            Text selectedComponent = displayText.get(selectedLine);

            int mouseOffset = mouseX - position.x;
            int currentOffset = 0;
            selectedComponent.visit()

            for (Text lineComponent : selectedComponent.) {
                currentOffset += fontRenderer.getWidth(lineComponent.asString());
                if (currentOffset >= mouseOffset) {
                    return lineComponent;
                }
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void updateComponentTextSize() {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int maxStringWidth = 0;
        int totalHeight = 0;
        for (ITextComponent textComponent : displayText) {
            maxStringWidth = Math.max(maxStringWidth, fontRenderer.getStringWidth(textComponent.getFormattedText()));
            totalHeight += fontRenderer.FONT_HEIGHT + 2;
        }
        totalHeight -= 2;
        setSize(new Size(maxStringWidth, totalHeight));
        if (uiAccess != null) {
            uiAccess.notifySizeChange();
        }
    }

    @SideOnly(Side.CLIENT)
    private void formatDisplayText() {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int maxTextWidthResult = maxWidthLimit == 0 ? Integer.MAX_VALUE : maxWidthLimit;
        this.displayText = displayText.stream()
            .flatMap(c -> GuiUtilRenderComponents.splitText(c, maxTextWidthResult, fontRenderer, true, true).stream())
            .collect(Collectors.toList());
    }

    @Override
    public void handleClientAction(int id, PacketBuffer buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            String componentData = buffer.readString(128);
            if (clickHandler != null) {
                clickHandler.accept(componentData, clickData);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private boolean handleCustomComponentClick(ITextComponent textComponent) {
        Style style = textComponent.getStyle();
        if (style.getClickEvent() != null) {
            ClickEvent clickEvent = style.getClickEvent();
            String componentText = clickEvent.getValue();
            if (clickEvent.getAction() == Action.OPEN_URL && componentText.startsWith("@!")) {
                String rawText = componentText.substring(2);
                ClickData clickData = new ClickData(Mouse.getEventButton(), isShiftDown(), isCtrlDown());
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
    @SideOnly(Side.CLIENT)
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        ITextComponent textComponent = getTextUnderMouse(mouseX, mouseY);
        if (textComponent != null) {
            if (handleCustomComponentClick(textComponent) ||
                getWrapScreen().handleComponentClick(textComponent)) {
                playButtonClickSound();
                return true;
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawInBackground(int mouseX, int mouseY, RenderContext context) {
        super.drawInBackground(mouseX, mouseY, context);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        Position position = getPosition();
        for (int i = 0; i < displayText.size(); i++) {
            fontRenderer.drawString(displayText.get(i).getFormattedText(), position.x, position.y + i * (fontRenderer.FONT_HEIGHT + 2), color);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawInForeground(int mouseX, int mouseY) {
        super.drawInForeground(mouseX, mouseY);
        ITextComponent component = getTextUnderMouse(mouseX, mouseY);
        if (component != null) {
            getWrapScreen().handleComponentHover(component, mouseX, mouseY);
        }
    }

    /**
     * Used to call mc-related chat component handling code,
     * for example component hover rendering and default click handling
     */
    @SideOnly(Side.CLIENT)
    private static class WrapScreen extends GuiScreen {
        @Override
        public void handleComponentHover(ITextComponent component, int x, int y) {
            super.handleComponentHover(component, x, y);
        }

        @Override
        public boolean handleComponentClick(ITextComponent component) {
            return super.handleComponentClick(component);
        }

        @Override
        protected void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
            GuiUtils.drawHoveringText(textLines, x, y, width, height, 256, font);
        }
    }
}
