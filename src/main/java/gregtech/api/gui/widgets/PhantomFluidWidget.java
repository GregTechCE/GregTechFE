package gregtech.api.gui.widgets;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.collect.Lists;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.util.GuiUtils;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.igredient.FluidIngredientTarget;
import gregtech.api.gui.igredient.IGhostIngredientTarget;
import gregtech.api.gui.igredient.IIngredientSlot;
import gregtech.api.gui.resources.RenderUtil;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.util.Position;
import gregtech.api.gui.util.Size;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PhantomFluidWidget extends Widget implements IIngredientSlot, IGhostIngredientTarget {

    private final FixedFluidInv fluidInventory;
    private final int slotIndex;

    protected TextureArea backgroundTexture = GuiTextures.FLUID_SLOT;

    protected FluidVolume lastFluidStack = FluidVolumeUtil.EMPTY;

    public PhantomFluidWidget(int xPosition, int yPosition, int width, int height, FixedFluidInv fluidInventory, int slotIndex) {
        super(new Position(xPosition, yPosition), new Size(width, height));
        this.fluidInventory = fluidInventory;
        this.slotIndex = slotIndex;
    }

    public PhantomFluidWidget setBackgroundTexture(TextureArea backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    @Override
    public void detectAndSendChanges() {
        FluidVolume currentStack = fluidInventory.getInvFluid(slotIndex);

        if (!currentStack.equals(lastFluidStack)) {
            this.lastFluidStack = currentStack.copy();
            writeUpdateInfo(1, currentStack::toMcBuffer);
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketByteBuf buffer) {
        if (id == 1) {
            try {
                this.lastFluidStack = FluidVolume.fromMcBuffer(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getIngredientOverMouse(int mouseX, int mouseY) {
        if (isMouseOverElement(mouseX, mouseY)) {
            return lastFluidStack;
        }
        return null;
    }

    @Override
    public List<Target> getPhantomTargets(Object ingredient) {
        FluidVolume ingredientFluidVolume = FluidIngredientTarget.retrieveFluidFromIngredient(ingredient);
        if (!ingredientFluidVolume.isEmpty()) {
            Rectangle rect = toRectangleBox();
            Consumer<FluidVolume> consumer = fluidVolume -> writeClientAction(2, fluidVolume::toMcBuffer);
            return Lists.newArrayList(new FluidIngredientTarget(rect, consumer));
        }
        return Collections.emptyList();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            writeClientAction(1, buffer -> {});
            return true;
        }
        return false;
    }

    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        if (id == 1) {
            ItemStack itemStack = gui.entityPlayer.currentScreenHandler.getCursorStack();
            if (!itemStack.isEmpty()) {
                FluidExtractable extractable = FluidAttributes.EXTRACTABLE.get(itemStack);
                FluidVolume extractedVolume = extractable.attemptAnyExtraction(FluidAmount.A_MILLION, Simulation.SIMULATE);

                if (!extractedVolume.isEmpty()) {
                    this.fluidInventory.setInvFluid(slotIndex, extractedVolume, Simulation.ACTION);
                }
            }
        } else if (id == 2) {
            try {
                FluidVolume newFluidVolume = FluidVolume.fromMcBuffer(buffer);
                this.fluidInventory.setInvFluid(slotIndex, newFluidVolume, Simulation.ACTION);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        Position pos = getPosition();
        Size size = getSize();
        if (backgroundTexture != null) {
            backgroundTexture.draw(matrices, pos.x, pos.y, size.width, size.height);
        }
        if (!lastFluidStack.isEmpty()) {
            RenderUtil.drawFluidForGui(matrices, lastFluidStack, lastFluidStack.amount(), pos.x + 1, pos.y + 1, size.width - 1, size.height - 1);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInForeground(MatrixStack matrices, int mouseX, int mouseY, RenderContext renderContext) {
        if (isMouseOverElement(mouseX, mouseY)) {
            if (!lastFluidStack.isEmpty()) {
                List<Text> lines = lastFluidStack.getFullTooltip();
                GuiUtils.renderTooltip(matrices, lines, Optional.empty(), mouseX, mouseY);
            }
        }
    }
}
