package gregtech.api.items.toolitem;

import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.tool.ScrewdriverItem;
import gregtech.api.items.metaitem.stats.IItemCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ScrewdriverItemStat implements IItemCapabilityProvider {

    @Override
    public ICapabilityProvider createProvider(ItemStack itemStack) {
        return new CapabilityProvider(itemStack);
    }

    private static class CapabilityProvider extends AbstractToolItemCapabilityProvider<ScrewdriverItem> implements ScrewdriverItem {

        public CapabilityProvider(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        protected Capability<ScrewdriverItem> getCapability() {
            return GTAttributes.CAPABILITY_SCREWDRIVER;
        }
    }
}
