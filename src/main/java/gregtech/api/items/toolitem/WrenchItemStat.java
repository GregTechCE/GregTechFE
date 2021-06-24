package gregtech.api.items.toolitem;

import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.tool.WrenchItem;
import gregtech.api.items.metaitem.stats.IItemCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class WrenchItemStat implements IItemCapabilityProvider {

    @Override
    public ICapabilityProvider createProvider(ItemStack itemStack) {
        return new CapabilityProvider(itemStack);
    }

    private static class CapabilityProvider extends AbstractToolItemCapabilityProvider<WrenchItem> implements WrenchItem {

        public CapabilityProvider(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        protected Capability<WrenchItem> getCapability() {
            return GTAttributes.CAPABILITY_WRENCH;
        }
    }
}
