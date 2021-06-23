package gregtech.api.gui.widgets.slot;

import alexiil.mc.lib.attributes.item.FixedItemInv;
import gregtech.api.gui.NativeWidget;
import gregtech.api.gui.RenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import gregtech.mixin.SlotMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.awt.*;

public class SlotWidget extends Widget implements NativeWidget {

    protected Slot slotReference;
    protected boolean isEnabled = true;

    protected boolean canTakeItems;
    protected boolean canPutItems;
    protected SlotLocationInfo locationInfo = new SlotLocationInfo(false, false);

    protected TextureArea[] backgroundTexture = new TextureArea[0];
    protected Rectangle scissor;

    public SlotWidget(Inventory inventory, int slotIndex, int xPosition, int yPosition, boolean canTakeItems, boolean canPutItems) {
        super(new Position(xPosition, yPosition), new Size(18, 18));
        this.canTakeItems = canTakeItems;
        this.canPutItems = canPutItems;
        this.slotReference = createSlot(inventory, slotIndex);
    }

    public SlotWidget(FixedItemInv itemHandler, int slotIndex, int xPosition, int yPosition, boolean canTakeItems, boolean canPutItems) {
        super(new Position(xPosition, yPosition), new Size(18, 18));
        this.canTakeItems = canTakeItems;
        this.canPutItems = canPutItems;
        this.slotReference = createSlot(itemHandler, slotIndex);
    }

    public SlotWidget(FixedItemInv itemHandler, int slotIndex, int xPosition, int yPosition) {
        this(itemHandler, slotIndex, xPosition, yPosition, true, true);
    }

    public SlotWidget(Inventory inventory, int slotIndex, int xPosition, int yPosition) {
        this(inventory, slotIndex, xPosition, yPosition, true, true);
    }

    protected Slot createSlot(Inventory inventory, int index) {
        return new WidgetSlot(this, inventory, index, 0, 0);
    }

    protected Slot createSlot(FixedItemInv itemHandler, int index) {
        return new WidgetSlotItemHandler(this, itemHandler, index, 0, 0);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(MatrixStack matrices, int mouseX, int mouseY, float deltaTicks, RenderContext renderContext) {
        if (isEnabled()) {
            Position pos = getPosition();
            Size size = getSize();
            for (TextureArea backgroundTexture : this.backgroundTexture) {
                backgroundTexture.draw(matrices, pos.x, pos.y, size.width, size.height);
            }
        }
    }

    @Override
    protected void onPositionUpdate() {
        if (slotReference != null && sizes != null) {
            Position position = getPosition();
            int newX = position.x + 1 - sizes.getGuiLeft();
            int newY = position.y + 1 - sizes.getGuiTop();

            ((SlotMixin) this.slotReference).setX(newX);
            ((SlotMixin) this.slotReference).setY(newY);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public void applyScissor(final int parentX, final int parentY, final int parentWidth, final int parentHeight) {
        this.scissor = new Rectangle(parentX, parentY, parentWidth, parentHeight);
    }

    @Override
    public Rectangle getScissor() {
        return scissor;
    }

    @Override
    public void detectAndSendChanges() {
    }

    /**
     * Sets array of background textures used by slot
     * they are drawn on top of each other
     */
    public SlotWidget setBackgroundTexture(TextureArea... backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    public SlotWidget setLocationInfo(boolean isPlayerInventory, boolean isHotbarSlot) {
        this.locationInfo = new SlotLocationInfo(isPlayerInventory, isHotbarSlot);
        return this;
    }

    @Override
    public SlotLocationInfo getSlotLocationInfo() {
        return locationInfo;
    }

    public boolean canPutStack(ItemStack stack) {
        return isEnabled() && canPutItems;
    }

    public boolean canTakeStack(PlayerEntity player) {
        return isEnabled() && canTakeItems;
    }

    public boolean isEnabled() {
        if (!this.isEnabled) {
            return false;
        }
        if (this.scissor == null) {
            return true;
        }
        return scissor.intersects(toRectangleBox());
    }

    @Override
    public boolean canMergeSlot(ItemStack stack) {
        return isEnabled();
    }

    public void onSlotChanged() {
        gui.holder.markDirty();
    }

    @Override
    public boolean onSlotClick(int button, SlotActionType clickTypeIn, PlayerEntity player) {
        return false;
    }

    @Override
    public final Slot getHandle() {
        return slotReference;
    }

}
