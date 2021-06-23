package gregtech.api.gui.impl;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.NativeWidget;
import gregtech.api.gui.Widget;
import gregtech.api.gui.WidgetUIAccess;
import gregtech.api.net.PacketUIClientAction;
import gregtech.api.net.PacketUIWidgetUpdate;
import gregtech.api.util.GTUtility;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModularUIScreenHandler extends ScreenHandler implements WidgetUIAccess {

    protected final HashMap<Slot, NativeWidget> slotMap = new HashMap<>();
    private final ModularUI modularUI;
    private int itemsMergedThisTick = 0;

    private boolean accumulateWidgetUpdateData = false;
    private final List<PacketUIWidgetUpdate> accumulatedUpdates = new ArrayList<>();

    public ModularUIScreenHandler(ModularUI modularUI, int syncId) {
        super(null, syncId);
        this.modularUI = modularUI;
        modularUI.guiWidgets.values().forEach(widget -> widget.setUiAccess(this));

        modularUI.guiWidgets.values().stream()
                .flatMap(widget -> widget.getNativeWidgets().stream())
                .forEach(nativeWidget -> {
                    Slot slot = nativeWidget.getHandle();
                    slotMap.put(slot, nativeWidget);
                    addSlot(slot);
                });
        modularUI.triggerOpenListeners();
    }

    public ModularUI getModularUI() {
        return modularUI;
    }

    @Override
    public void close(PlayerEntity playerEntity) {
        super.close(playerEntity);
        modularUI.triggerCloseListeners();
    }

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();
        this.itemsMergedThisTick = 0;
        for (Widget widget : modularUI.guiWidgets.values()) {
            widget.detectAndSendChanges();
        }
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        Slot slot = getSlot(slotIndex);
        NativeWidget nativeWidget = slotMap.get(slot);

        if (nativeWidget != null) {
            boolean cancelVanillaLogic = nativeWidget.onSlotClick(button, actionType, player);
            if (cancelVanillaLogic) {
                return;
            }
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }

    private List<NativeWidget> getShiftClickSlots(ItemStack itemStack, boolean fromContainer) {
        return slotMap.values().stream()
            .filter(it -> it.canMergeSlot(itemStack))
            .filter(it -> it.getSlotLocationInfo().isPlayerInventory == fromContainer)
            .sorted(Comparator.comparing(s -> (fromContainer ? -1 : 1) * s.getHandle().id))
            .collect(Collectors.toList());
    }

    @Override
    public boolean attemptMergeStack(ItemStack itemStack, boolean fromContainer, boolean simulate) {
        List<Slot> inventorySlots = getShiftClickSlots(itemStack, fromContainer).stream()
            .map(NativeWidget::getHandle)
            .collect(Collectors.toList());
        return GTUtility.mergeItemStack(itemStack, inventorySlots, simulate);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        Slot slot = getSlot(index);
        NativeWidget nativeWidget = this.slotMap.get(slot);

        if (!slot.canTakeItems(player)) {
            return ItemStack.EMPTY;
        }
        if (!slot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = slot.getStack();
        ItemStack stackToMerge = stackInSlot.copy();
        if (nativeWidget != null) {
            nativeWidget.onItemTake(player, stackToMerge, true);
        }

        boolean fromPlayerInventory = slot.inventory instanceof PlayerInventory;
        if (nativeWidget != null) {
            fromPlayerInventory = nativeWidget.getSlotLocationInfo().isPlayerInventory;
        }

        if (!attemptMergeStack(stackToMerge, !fromPlayerInventory, true)) {
            return ItemStack.EMPTY;
        }

        int itemsMerged;
        if (stackToMerge.isEmpty() || (nativeWidget != null && nativeWidget.canMergeSlot(stackToMerge))) {
            itemsMerged = stackInSlot.getCount() - stackToMerge.getCount();
        } else {
            //if we can't have partial stack merge, we have to use all the stack
            itemsMerged = stackInSlot.getCount();
        }

        //we can merge at most one stack at a time
        int itemsToExtract = itemsMerged;
        itemsMerged += this.itemsMergedThisTick;

        if (itemsMerged > stackInSlot.getMaxCount()) {
            return ItemStack.EMPTY;
        }
        this.itemsMergedThisTick += itemsToExtract;

        //otherwise, perform extraction and merge
        ItemStack extractedStack = stackInSlot.split(itemsToExtract);
        if (stackInSlot.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        if (nativeWidget != null) {
            nativeWidget.onItemTake(player, extractedStack, false);
        }
        ItemStack resultStack = extractedStack.copy();

        if (!attemptMergeStack(extractedStack, !fromPlayerInventory, false)) {
            resultStack = ItemStack.EMPTY;
        }

        if (!extractedStack.isEmpty()) {
            player.dropItem(extractedStack, false, false);
            resultStack = ItemStack.EMPTY;
        }
        return resultStack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        NativeWidget nativeWidget = slotMap.get(slot);
        return nativeWidget != null && nativeWidget.canMergeSlot(stack);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return getModularUI().holder.isValid();
    }

    @Override
    public void notifySizeChange() {
    }

    //WARNING! WIDGET CHANGES SHOULD BE *STRICTLY* SYNCHRONIZED BETWEEN SERVER AND CLIENT,
    //OTHERWISE ID MISMATCH CAN HAPPEN BETWEEN ASSIGNED SLOTS!
    @Override
    public void notifyWidgetChange() {
        List<NativeWidget> nativeWidgets = modularUI.guiWidgets.values().stream()
                .flatMap(widget -> widget.getNativeWidgets().stream())
                .collect(Collectors.toList());

        Set<NativeWidget> removedWidgets = new HashSet<>(slotMap.values());
        nativeWidgets.forEach(removedWidgets::remove);

        if(!removedWidgets.isEmpty()) {
            for(NativeWidget removedWidget : removedWidgets) {
                Slot slotHandle = removedWidget.getHandle();
                this.slotMap.remove(slotHandle);

                //replace removed slot with empty placeholder to avoid list index shift
                EmptySlotPlaceholder emptySlotPlaceholder = new EmptySlotPlaceholder();
                emptySlotPlaceholder.id = slotHandle.id;
                this.slots.set(slotHandle.id, emptySlotPlaceholder);
            }
        }

        Set<NativeWidget> addedWidgets = new HashSet<>(nativeWidgets);
        addedWidgets.removeAll(slotMap.values());

        if(!addedWidgets.isEmpty()) {
            int[] emptySlotIndexes = this.slots.stream()
                    .filter(it -> it instanceof EmptySlotPlaceholder)
                    .mapToInt(slot -> slot.id)
                    .toArray();

            int currentIndex = 0;

            for(NativeWidget addedWidget : addedWidgets) {
                Slot slotHandle = addedWidget.getHandle();
                this.slotMap.put(slotHandle, addedWidget);

                //add or replace empty slot in inventory
                if(currentIndex < emptySlotIndexes.length) {
                    int slotIndex = emptySlotIndexes[currentIndex++];
                    slotHandle.id = slotIndex;
                    this.slots.set(slotIndex, slotHandle);
                } else {
                    slotHandle.id = this.slots.size();
                    this.slots.add(slotHandle);
                }
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void writeClientAction(Widget widget, int updateId, Consumer<PacketByteBuf> payloadWriter) {
        int widgetId = modularUI.guiWidgets.inverse().get(widget);
        PacketByteBuf packetBuffer = new PacketByteBuf(Unpooled.buffer());
        packetBuffer.writeVarInt(updateId);
        payloadWriter.accept(packetBuffer);

        if (modularUI.entityPlayer instanceof ClientPlayerEntity) {
            PacketUIClientAction widgetUpdate = new PacketUIClientAction(this.syncId, widgetId, packetBuffer);
            widgetUpdate.sendToClient();
        }
    }

    public List<PacketUIWidgetUpdate> accumulateWidgetUpdates() {
        this.accumulateWidgetUpdateData = true;
        this.modularUI.guiWidgets.values().forEach(Widget::detectAndSendChanges);
        this.accumulateWidgetUpdateData = false;

        ArrayList<PacketUIWidgetUpdate> updates = new ArrayList<>(this.accumulatedUpdates);
        this.accumulatedUpdates.clear();
        return updates;
    }

    @Override
    public void writeUpdateInfo(Widget widget, int updateId, Consumer<PacketByteBuf> payloadWriter) {
        int widgetId = modularUI.guiWidgets.inverse().get(widget);
        PacketByteBuf packetBuffer = new PacketByteBuf(Unpooled.buffer());
        packetBuffer.writeVarInt(updateId);
        payloadWriter.accept(packetBuffer);

        if (modularUI.entityPlayer instanceof ServerPlayerEntity playerEntity) {
            var widgetUpdate = new PacketUIWidgetUpdate(this.syncId, widgetId, packetBuffer);
            if (accumulateWidgetUpdateData) {
                this.accumulatedUpdates.add(widgetUpdate);
            } else {
                widgetUpdate.sendTo(playerEntity);
            }
        }
    }

}
