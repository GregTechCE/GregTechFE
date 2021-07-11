package gregtech.api.render.variant;

import gregtech.api.items.util.ModelProviderItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ModelProviderItemHandler implements GTModelVariantProvider.ItemModelProvider {

    @Override
    public @Nullable UnbakedModel loadItemModel(Item item, ModelProviderContext context) {
        if (item instanceof ModelProviderItem modelProviderItem) {
            return context.loadModel(modelProviderItem.getModelLocation());
        }
        return null;
    }
}
