package gregtech.api.item.util;

import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface ModelStateProviderItem {

    default Identifier getStateModelLocation() {
        Identifier itemId = Registry.ITEM.getId((Item) this);
        return new Identifier(itemId.getNamespace(), "item/" + itemId.getPath());
    }

    ModelStateManager<Item> getModelStateManager();

    ModelState<Item> getModelState(ItemStack itemStack);
}
