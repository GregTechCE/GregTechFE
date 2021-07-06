package gregtech.api.gui.widgets;

import alexiil.mc.lib.attributes.fluid.*;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.EmptyFluidExtractable;
import alexiil.mc.lib.attributes.fluid.impl.RejectingFluidInsertable;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.NullVariant;
import gregtech.api.gui.util.GuiUtils;
import gregtech.api.gui.util.InputHelper;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.igredient.IIngredientSlot;
import gregtech.api.gui.resources.EmptyTextureArea;
import gregtech.api.gui.resources.RenderUtil;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TankWidget extends Widget implements IIngredientSlot {

    private final FixedFluidInv fluidInventory;
    private final int slotIndex;

    private FluidVolume lastFluidInTank = FluidVolumeUtil.EMPTY;
    private FluidAmount lastTankCapacity = FluidAmount.BUCKET;

    private int fluidRenderOffset = 1;
    private boolean alwaysShowFull;

    private boolean allowClickContainerFilling;
    private boolean allowClickContainerEmptying;

    private TextureArea[] backgroundTexture = new TextureArea[0];
    private TextureArea overlayTexture = EmptyTextureArea.INSTANCE;

    public TankWidget(FixedFluidInv fluidInventory, int slotIndex, int x, int y, int width, int height) {
        super(new Position(x, y), new Size(width, height));
        this.fluidInventory = fluidInventory;
        this.slotIndex = slotIndex;
    }

    @Override
    public Object getIngredientOverMouse(int mouseX, int mouseY) {
        if (isMouseOverElement(mouseX, mouseY)) {
            return lastFluidInTank;
        }
        return null;
    }

    @Override
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        Position pos = getPosition();
        Size size = getSize();

        for (TextureArea textureArea : backgroundTexture) {
            textureArea.draw(matrices, pos.x, pos.y, size.width, size.height);
        }

        FluidVolume fluidInTank = this.lastFluidInTank;
        FluidAmount tankCapacity = this.lastTankCapacity;
        if (this.alwaysShowFull) {
            tankCapacity = fluidInTank.amount();
        }

        if (!fluidInTank.isEmpty()) {
            int x = pos.x + fluidRenderOffset;
            int y = pos.y + fluidRenderOffset;
            int width = size.width - fluidRenderOffset;
            int height = size.height - fluidRenderOffset;

            RenderUtil.drawFluidForGui(matrices, fluidInTank, tankCapacity, x, y, width, height);
        }

        double bucketsAmount = lastFluidInTank.amount().div(FluidAmount.BUCKET).asInexactDouble();
        if (alwaysShowFull && bucketsAmount > 0) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            String text = String.format("%.1fB", bucketsAmount);

            int x = pos.x + 1 + size.width - 2 - textRenderer.getWidth(text);
            int y = pos.y + (size.height / 3) + 3;
            textRenderer.draw(matrices, text, x, y, 0xFFFFFF);
        }

        overlayTexture.draw(matrices, pos.x, pos.y, size.width, size.height);
    }

    @Override
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY, RenderContext renderContext) {
        if (isMouseOverElement(mouseX, mouseY)) {
            List<Text> tooltips = new ArrayList<>();
            lastFluidInTank.addFullTooltip(lastTankCapacity, tooltips);

            boolean canFillTank = !lastFluidInTank.amount().equals(lastTankCapacity);
            boolean canEmptyTank = !lastFluidInTank.isEmpty();
            if (allowClickContainerFilling && canEmptyTank) {
                tooltips.add(new TranslatableText("gregtech.fluid.click_to_fill"));
                tooltips.add(new TranslatableText("gregtech.fluid.click_to_fill.shift"));
            }
            if (allowClickContainerEmptying && canFillTank) {
                tooltips.add(new TranslatableText("gregtech.fluid.click_to_empty"));
                tooltips.add(new TranslatableText("gregtech.fluid.click_to_empty.shift"));
            }
            GuiUtils.renderTooltip(matrices, tooltips, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    public void detectAndSendChanges() {
        FluidAmount tankCapacity = this.fluidInventory.getMaxAmount_F(slotIndex);

        if (!tankCapacity.equals(this.lastTankCapacity)) {
            this.lastTankCapacity = tankCapacity;
            writeUpdateInfo(1, tankCapacity::toMcBuffer);
        }

        FluidVolume fluidVolume = this.fluidInventory.getInvFluid(slotIndex);

        if (!fluidVolume.equals(this.lastFluidInTank)) {
            this.lastFluidInTank = fluidVolume.copy();
            writeUpdateInfo(2, fluidVolume::toMcBuffer);
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        if (id == 1) {
            this.lastTankCapacity = FluidAmount.fromMcBuffer(buffer);
        }
        if (id == 2) {
            try {
                this.lastFluidInTank = FluidVolume.fromMcBuffer(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        if (id == 1) {
            boolean isShiftKeyDown = buffer.readBoolean();
            tryClickContainer(isShiftKeyDown);
        }
    }

    private void tryClickContainer(boolean isShiftKeyDown) {
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) gui.entityPlayer;
        SingleFluidTank tankFluidInventory = this.fluidInventory.getTank(slotIndex);

        ItemStack cursorStack = playerEntity.currentScreenHandler.getCursorStack();
        int maxActionAttempts = isShiftKeyDown ? cursorStack.getCount() : 1;

        if (cursorStack.isEmpty()) {
            return;
        }

        FluidInsertable fluidInsertable = tankFluidInventory;
        if (!allowClickContainerEmptying) {
            fluidInsertable = RejectingFluidInsertable.NULL;
        }

        FluidExtractable fluidExtractable = tankFluidInventory;
        if (!allowClickContainerFilling) {
            fluidExtractable = EmptyFluidExtractable.NULL;
        }

        for (int i = 0; i < maxActionAttempts; i++) {
            FluidVolumeUtil.FluidTankInteraction result = FluidInvUtil.interactCursorWithTank(
                    fluidInsertable, fluidExtractable, playerEntity);

            //Exit as soon as we couldn't move anything in a single operation
            if (!result.didMoveAny()) {
                break;
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY) && button == 0) {
            PlayerEntity playerEntity = gui.entityPlayer;
            ItemStack cursorStack = playerEntity.currentScreenHandler.getCursorStack();

            FluidExtractable containerExtractable = FluidAttributes.EXTRACTABLE.get(cursorStack);
            FluidInsertable containerInsertable = FluidAttributes.INSERTABLE.get(cursorStack);

            boolean canDrainContainer = allowClickContainerEmptying &&
                    !(containerExtractable instanceof NullVariant);
            boolean canFillContainer = allowClickContainerFilling &&
                    !(containerInsertable instanceof NullVariant);

            if (canDrainContainer || canFillContainer) {
                boolean isShiftKeyDown = InputHelper.isShiftDown();
                writeClientAction(1, writer -> writer.writeBoolean(isShiftKeyDown));
                InputHelper.playButtonClickSound();
                return true;
            }
        }
        return false;
    }

    public TankWidget setAlwaysShowFull(boolean alwaysShowFull) {
        this.alwaysShowFull = alwaysShowFull;
        return this;
    }

    public TankWidget setBackgroundTexture(TextureArea... backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    public TankWidget setOverlayTexture(TextureArea overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    public TankWidget setFluidRenderOffset(int fluidRenderOffset) {
        this.fluidRenderOffset = fluidRenderOffset;
        return this;
    }

    public TankWidget setContainerClicking(boolean allowClickContainerFilling, boolean allowClickContainerEmptying) {
        this.allowClickContainerFilling = allowClickContainerFilling;
        this.allowClickContainerEmptying = allowClickContainerEmptying;
        return this;
    }
}
