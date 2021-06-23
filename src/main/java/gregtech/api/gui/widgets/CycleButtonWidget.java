package gregtech.api.gui.widgets;

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
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;

public class CycleButtonWidget extends Widget {

    protected TextureArea buttonTexture = GuiTextures.VANILLA_BUTTON.getSubArea(0.0f, 0.0f, 1.0f, 0.5f);
    private final List<String> optionNames;
    private int textColor = 0xFFFFFF;
    private final IntSupplier currentOptionSupplier;
    private final IntConsumer setOptionExecutor;
    protected int currentOption;
    protected String tooltipHoverString;
    protected long hoverStartTime = -1L;
    protected boolean isMouseHovered;

    public CycleButtonWidget(int xPosition, int yPosition, int width, int height, String[] optionNames,
                             IntSupplier currentOptionSupplier, IntConsumer setOptionExecutor) {
        super(new Position(xPosition, yPosition), new Size(width, height));

        this.optionNames = Arrays.asList(optionNames);
        this.currentOptionSupplier = currentOptionSupplier;
        this.setOptionExecutor = setOptionExecutor;
    }

    public <T extends Enum<T> & StringIdentifiable> CycleButtonWidget(int xPosition, int yPosition,
                                                                      int width, int height, Class<T> enumClass,
                                                                      Supplier<T> supplier, Consumer<T> updater) {
        super(new Position(xPosition, yPosition), new Size(width, height));

        T[] enumConstantPool = enumClass.getEnumConstants();
        this.optionNames = Arrays.stream(enumConstantPool)
                .map(StringIdentifiable::asString)
                .collect(Collectors.toList());

        this.currentOptionSupplier = () -> supplier.get().ordinal();
        this.setOptionExecutor = (newIndex) -> updater.accept(enumConstantPool[newIndex]);
    }

    public CycleButtonWidget(int xPosition, int yPosition, int width, int height,
                             BooleanSupplier supplier, BooleanConsumer updater, String... optionNames) {
        super(new Position(xPosition, yPosition), new Size(width, height));

        this.optionNames = Arrays.asList(optionNames);
        this.currentOptionSupplier = () -> supplier.getAsBoolean() ? 1 : 0;
        this.setOptionExecutor = (value) -> updater.apply(value >= 1);
    }

    public CycleButtonWidget setTooltipHoverString(String hoverString) {
        this.tooltipHoverString = hoverString;
        return this;
    }

    public CycleButtonWidget setButtonTexture(TextureArea texture) {
        this.buttonTexture = texture;
        return this;
    }

    public CycleButtonWidget setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, RenderContext context) {
        Position pos = getPosition();
        Size size = getSize();

        if (buttonTexture instanceof SizedTextureArea) {
            ((SizedTextureArea) buttonTexture).drawHorizontalCutSubArea(matrices, pos.x, pos.y, size.width, size.height, 0.0f, 1.0f);
        } else {
            buttonTexture.drawSubArea(matrices, pos.x, pos.y, size.width, size.height, 0.0f, 0.0f, 1.0f, 1.0f);
        }

        String displayName = optionNames.get(currentOption);
        GuiUtils.drawString(matrices, I18n.translate(displayName),
                pos.x + size.width / 2,
                pos.y + size.height / 2,
                textColor, true);
    }

    @Override
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY) {
        boolean isHovered = isMouseOverElement(mouseX, mouseY);
        boolean wasHovered = isMouseHovered;
        if (isHovered && !wasHovered) {
            this.isMouseHovered = true;
            this.hoverStartTime = System.currentTimeMillis();
        } else if (!isHovered && wasHovered) {
            this.isMouseHovered = false;
            this.hoverStartTime = 0L;
        } else if (isHovered) {
            long timeSinceHover = System.currentTimeMillis() - hoverStartTime;
            if (timeSinceHover > 1000L && tooltipHoverString != null) {
                List<Text> hoverList = Arrays.stream(I18n.translate(tooltipHoverString).split("/n"))
                        .map(LiteralText::new)
                        .collect(Collectors.toList());
                GuiUtils.renderTooltip(matrices, hoverList, Optional.empty(), mouseX, mouseY);
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (currentOptionSupplier.getAsInt() != currentOption) {
            this.currentOption = currentOptionSupplier.getAsInt();
            writeUpdateInfo(1, buf -> buf.writeVarInt(currentOption));
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == 1) {
            this.currentOption = buffer.readVarInt();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (isMouseOverElement(mouseX, mouseY)) {
            //Allow only the RMB to reverse cycle
            if(button == 1) {
                //Wrap from the first option to the last if needed
                this.currentOption = currentOption == 0 ? optionNames.size() - 1 : currentOption - 1;
            }
            else {
                this.currentOption = (currentOption + 1) % optionNames.size();
            }
            writeClientAction(1, buf -> buf.writeVarInt(currentOption));
            InputHelper.playButtonClickSound();
            return true;
        }
        return false;
    }


    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            this.currentOption = MathHelper.clamp(buffer.readVarInt(), 0, optionNames.size());
            setOptionExecutor.accept(currentOption);
        }
    }

}
