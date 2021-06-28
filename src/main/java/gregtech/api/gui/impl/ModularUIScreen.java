package gregtech.api.gui.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import gregtech.api.gui.*;
import gregtech.api.gui.util.GuiUtils;
import gregtech.api.gui.util.ScissorStack;
import gregtech.mixin.accessor.HandledScreenAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class ModularUIScreen extends HandledScreen<ModularUIScreenHandler> implements RenderContext {

    private final ModularUI modularUI;

    public ModularUIScreen(int syncId, PlayerInventory inventory, ModularUI modularUI) {
        super(new ModularUIScreenHandler(modularUI, syncId), inventory, new LiteralText(""));
        this.modularUI = modularUI;
    }

    public ModularUI getModularUI() {
        return modularUI;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.disableDepthTest();

        for (Drawable drawable : ((HandledScreenAccessor) this).getDrawables()) {
            drawable.render(matrices, mouseX, mouseY, delta);
        }

        refreshFocusedSlot(mouseX, mouseY);
        drawBackground(matrices, delta, mouseX, mouseY);

        drawSlotContents(matrices);
        drawFocusedSlotOutline(matrices);

        drawForeground(matrices, mouseX, mouseY);

        drawDraggedItemStack(mouseX, mouseY);
        drawTouchDropReturningStack();

        RenderSystem.enableDepthTest();
    }

    private void applySlotScissor(Slot slot, Runnable action) {
        Rectangle scissor = null;
        if (slot instanceof NativeWidget nativeWidget) {
            scissor = nativeWidget.getScissor();
        }

        if (scissor != null) {
            ScissorStack.pushScissorFrame(scissor.x, scissor.y, scissor.width, scissor.height);
        }
        action.run();
        if (scissor != null) {
            ScissorStack.popScissorFrame();
        }
    }

    private void drawFocusedSlotOutline(MatrixStack matrices) {
        if (this.focusedSlot != null) {
            applySlotScissor(focusedSlot, () -> {
                int slotAbsoluteX = this.x + focusedSlot.x;
                int slotAbsoluteY = this.y + focusedSlot.y;
                GuiUtils.drawSelectionBox(matrices, slotAbsoluteX, slotAbsoluteY, 16, 16, getZOffset());
            });
        }
    }

    private void refreshFocusedSlot(int mouseX, int mouseY) {
        this.focusedSlot = null;

        for (Slot slot : this.handler.slots) {
            if (slot.isEnabled()) {
                int slotAbsoluteX = this.x + slot.x;
                int slotAbsoluteY = this.y + slot.y;

                if (isPointWithinBounds(slotAbsoluteX, slotAbsoluteY, 16, 16, mouseX, mouseY)) {
                    this.focusedSlot = slot;
                }
            }
        }
    }

    private void drawSlotAbsolute(MatrixStack matrices, Slot slot) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(this.x, this.y, 0.0);
        ((HandledScreenAccessor) this).drawSlot(matrices, slot);
        matrixStack.pop();
    }

    private void drawSlotContents(MatrixStack matrices) {
        for(Slot slot : this.handler.slots) {
            if (slot.isEnabled()) {
                applySlotScissor(slot, () -> drawSlotAbsolute(matrices, slot));
            }
        }
    }

    private void drawDraggedItemStack(int mouseX, int mouseY) {
        HandledScreenAccessor mixin = (HandledScreenAccessor) this;

        ItemStack draggedStack = mixin.getTouchDragStack();
        boolean isTouchDragged = true;
        boolean touchIsRightClickDrag = mixin.isTouchIsRightClickDrag();

        int draggedStackRemainder = mixin.getDraggedStackRemainder();
        if (draggedStack.isEmpty()) {
            draggedStack = this.handler.getCursorStack();
            isTouchDragged = false;
        }

        if (!draggedStack.isEmpty()) {
            int stackXOffset = 8;
            int stackYOffset = isTouchDragged ? 16 : 8;
            String alternativeText = null;

            if (isTouchDragged && touchIsRightClickDrag) {
                draggedStack = draggedStack.copy();
                draggedStack.setCount(MathHelper.ceil(draggedStack.getCount() / 2.0f));

            } else if (cursorDragging && this.cursorDragSlots.size() > 1) {
                draggedStack = draggedStack.copy();
                draggedStack.setCount(draggedStackRemainder);

                if (draggedStack.isEmpty()) {
                    alternativeText = Formatting.YELLOW + "0";
                }
            }
            GuiUtils.drawItemStack(draggedStack, mouseX - stackXOffset, mouseY - stackYOffset, alternativeText);
        }
    }

    private void drawTouchDropReturningStack() {
        HandledScreenAccessor mixin = (HandledScreenAccessor) this;

        ItemStack touchDropReturningStack = mixin.getTouchDropReturningStack();
        float touchDropTime = mixin.getTouchDropTime();
        Slot touchDropOriginSlot = mixin.getTouchDropOriginSlot();

        int touchDropX = mixin.getTouchDropX();
        int touchDropY = mixin.getTouchDropY();

        if (!touchDropReturningStack.isEmpty()) {
            float animationProgress = (Util.getMeasuringTimeMs() - touchDropTime) / 100.0f;

            if (animationProgress >= 1.0F) {
                animationProgress = 1.0F;
                ((HandledScreenAccessor) this).setTouchDropReturningStack(ItemStack.EMPTY);
            }

            int touchDropDeltaX = touchDropOriginSlot.x - touchDropX;
            int touchDropDeltaY = touchDropOriginSlot.y - touchDropY;

            int xPos = this.x + touchDropX + (int)(touchDropDeltaX * animationProgress);
            int yPos = this.y + touchDropY + (int)(touchDropDeltaY * animationProgress);
            GuiUtils.drawItemStack(touchDropReturningStack, xPos, yPos, null);
        }
    }

    @Override
    public void init() {
        //noinspection ConstantConditions
        this.client.keyboard.setRepeatEvents(true);

        this.backgroundWidth = modularUI.getWidth();
        this.backgroundHeight = modularUI.getHeight();
        super.init();

        this.modularUI.updateScreenSize(width, height);
    }

    @Override
    public void removed() {
        super.removed();

        //noinspection ConstantConditions
        this.client.keyboard.setRepeatEvents(false);
    }

    @Override
    public void tick() {
        super.tick();

        for (Widget widget : modularUI.guiWidgets.values()) {
            widget.updateScreen();
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            widget.drawInForeground(matrices, mouseX, mouseY, this);
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        modularUI.backgroundPath.draw(matrices, x, y, backgroundWidth, backgroundHeight);

        for (Widget widget : modularUI.guiWidgets.values()) {
            widget.drawInBackground(matrices, mouseX, mouseY, delta, this);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            if (widget.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            if (widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            if (widget.mouseScrolled(mouseX, mouseY, amount)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            if (widget.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            if (widget.keyReleased(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (Widget widget : modularUI.guiWidgets.values()) {
            if (widget.charTyped(chr, modifiers)) {
                return true;
            }
        }
        return super.charTyped(chr, modifiers);
    }
}
