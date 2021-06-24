package gregtech.api.items.toolitem;

import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.tool.SoftHammerItem;
import gregtech.api.items.metaitem.stats.IItemCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class SoftMalletItemStat implements IItemCapabilityProvider {

    @Override
    public ICapabilityProvider createProvider(ItemStack itemStack) {
        return new CapabilityProvider(itemStack);
    }

    private static class CapabilityProvider extends AbstractToolItemCapabilityProvider<SoftHammerItem> implements SoftHammerItem {

        public CapabilityProvider(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        protected Capability<SoftHammerItem> getCapability() {
            return GTAttributes.CAPABILITY_MALLET;
        }
    }
}
