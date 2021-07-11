package gregtech.api.render.variant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class GTModelVariantProvider implements ModelVariantProvider {

    private final List<BlockModelProvider> blockModelProviders = new ArrayList<>();
    private final List<ItemModelProvider> itemModelProviders = new ArrayList<>();

    public GTModelVariantProvider(ResourceManager resourceManager) {
        registerDefaultProviders(resourceManager);
    }

    private void registerDefaultProviders(ResourceManager resourceManager) {
        this.blockModelProviders.add(new OreBlockHandler());
        this.blockModelProviders.add(new BlockStateModelProvider(resourceManager));
        this.itemModelProviders.add(new ModelProviderItemHandler());
    }

    @Nullable
    private UnbakedModel handleBlockModel(Block block, ModelProviderContext context) throws ModelProviderException {
        for (BlockModelProvider provider : this.blockModelProviders) {
            UnbakedModel blockModel = provider.loadBlockModel(block, context);

            if (blockModel != null) {
                return blockModel;
            }
        }
        return null;
    }

    @Nullable
    private UnbakedModel handleItemModel(Item item, ModelProviderContext context) throws ModelProviderException {
        for (ItemModelProvider provider : this.itemModelProviders) {
            UnbakedModel itemModel = provider.loadItemModel(item, context);

            if (itemModel != null) {
                return itemModel;
            }
        }

        Block itemBlock = Block.getBlockFromItem(item);
        if (itemBlock != Blocks.AIR) {
            return handleBlockModel(itemBlock, context);
        }
        return null;
    }

    @Override
    public @Nullable UnbakedModel loadModelVariant(ModelIdentifier modelId, ModelProviderContext context) throws ModelProviderException {
        if (modelId.getVariant().equals("inventory")) {
            Identifier itemId = new Identifier(modelId.getNamespace(), modelId.getPath());
            Item item = Registry.ITEM.get(itemId);

            return handleItemModel(item, context);
        } else {
            Identifier blockId = new Identifier(modelId.getNamespace(), modelId.getPath());
            Block block = Registry.BLOCK.get(blockId);

            return handleBlockModel(block, context);
        }
    }

    public static void register() {
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(GTModelVariantProvider::new);
    }

    @FunctionalInterface
    public interface ItemModelProvider {
        @Nullable UnbakedModel loadItemModel(Item item, ModelProviderContext context) throws ModelProviderException;
    }

    @FunctionalInterface
    public interface BlockModelProvider {
        @Nullable UnbakedModel loadBlockModel(Block block, ModelProviderContext context) throws ModelProviderException;
    }
}
