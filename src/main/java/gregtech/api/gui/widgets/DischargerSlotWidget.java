package gregtech.api.gui.widgets;

import alexiil.mc.lib.attributes.item.FixedItemInv;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.ElectricItem;
import gregtech.api.gui.widgets.slot.SlotWidget;
import net.minecraft.item.ItemStack;

public class DischargerSlotWidget extends SlotWidget {

    public DischargerSlotWidget(FixedItemInv itemHandler, int slotIndex, int xPosition, int yPosition) {
        super(itemHandler, slotIndex, xPosition, yPosition, true, true);
    }

    @Override
    public boolean canPutStack(ItemStack stack) {
        ElectricItem capability = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(stack);
        return capability != null && capability.canProvideChargeExternally();
    }
}
