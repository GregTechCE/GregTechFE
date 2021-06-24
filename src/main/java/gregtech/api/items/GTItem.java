package gregtech.api.items;

import alexiil.mc.lib.attributes.AttributeProviderItem;
import alexiil.mc.lib.attributes.ItemAttributeList;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Reference;
import gregtech.api.items.metaitem.ElectricStats;
import gregtech.api.items.metaitem.FluidStats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GTItem extends Item implements AttributeProviderItem {

    private final FluidStats fluidStats;
    private final ElectricStats electricStats;

    public GTItem(GTItemSettings settings) {
        super(settings);
        this.fluidStats = settings.fluidStats;
        this.electricStats = settings.electricStats;
    }



    @Override
    public void addAllAttributes(Reference<ItemStack> stack, LimitedConsumer<ItemStack> excess, ItemAttributeList<?> to) {

    }
}
