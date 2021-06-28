package gregtech.api.gui.widgets;

import alexiil.mc.lib.attributes.item.FixedItemInv;
import com.google.common.collect.Lists;
import gregtech.api.gui.util.InputHelper;
import gregtech.api.gui.igredient.IGhostIngredientTarget;
import gregtech.api.gui.igredient.ItemIngredientTarget;
import gregtech.api.gui.widgets.slot.SlotWidget;
import gregtech.api.gui.util.SlotUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.SlotActionType;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PhantomSlotWidget extends SlotWidget implements IGhostIngredientTarget {

    public PhantomSlotWidget(FixedItemInv itemHandler, int slotIndex, int xPosition, int yPosition) {
        super(itemHandler, slotIndex, xPosition, yPosition, false, true);
    }

    @Override
    public boolean onSlotClick(int dragType, SlotActionType clickTypeIn, PlayerEntity player) {
        ItemStack stackHeld = player.currentScreenHandler.getCursorStack();
        SlotUtil.slotClickPhantom(slotReference, dragType, clickTypeIn, stackHeld);
        return true;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack) {
        return false;
    }

    @Override
    public List<Target> getPhantomTargets(Object ingredient) {
        if (!(ingredient instanceof ItemStack)) {
            return Collections.emptyList();
        }

        Rectangle rectangle = toRectangleBox();

        Consumer<ItemStack> consumer = itemStack -> {
            int mouseButton = InputHelper.getActiveMouseButton();
            boolean shiftDown = InputHelper.isShiftDown();

            writeClientAction(1, buffer -> {
                buffer.writeItemStack(itemStack);
                buffer.writeVarInt(mouseButton);
                buffer.writeBoolean(shiftDown);
            });
        };

        return Lists.newArrayList(new ItemIngredientTarget(rectangle, consumer));
    }

    @Override
    public void handleClientAction(int id, PacketByteBuf buffer) {
        if (id == 1) {
            ItemStack stackHeld = buffer.readItemStack();
            int mouseButton = buffer.readVarInt();
            boolean shiftKeyDown = buffer.readBoolean();
            SlotActionType clickType = shiftKeyDown ? SlotActionType.QUICK_MOVE : SlotActionType.PICKUP;
            SlotUtil.slotClickPhantom(slotReference, mouseButton, clickType, stackHeld);
        }
    }
}
