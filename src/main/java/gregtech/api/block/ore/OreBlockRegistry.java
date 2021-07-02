package gregtech.api.block.ore;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OreBlockType;
import gregtech.api.unification.ore.OreVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class OreBlockRegistry {

    public static final OreBlockRegistry INSTANCE = new OreBlockRegistry();

    private final Map<OreBlockType, Block> registeredOreBlocks = new HashMap<>();

    private OreBlockRegistry() {
    }

    @NotNull
    public Block getOreBlock(OreBlockType oreBlockType) {
        return this.registeredOreBlocks.getOrDefault(oreBlockType, Blocks.AIR);
    }

    public Block getOreBlock(OreVariant variant, Material material) {
        return getOreBlock(new OreBlockType(variant, material));
    }

    public void registerOreBlocks() {
        for (Identifier materialId : Material.REGISTRY.getIds()) {
            Material material = Material.REGISTRY.get(materialId);

            for (Identifier variantId : OreVariant.REGISTRY.getIds()) {
                OreVariant oreVariant = OreVariant.REGISTRY.get(variantId);

                //noinspection ConstantConditions
                if (oreVariant.shouldGenerateFor(material)) {
                    OreBlockType blockType = new OreBlockType(oreVariant, material);
                    OreBlock oreBlock = createOreBlock(blockType);
                    OreBlockItem oreBlockItem = createOreBlockItem(oreBlock);

                    String blockRegistryName = oreVariant.createBlockName(materialId.getPath());
                    Identifier blockRegistryId = createBlockId(materialId, variantId, blockRegistryName);

                    Registry.register(Registry.BLOCK, blockRegistryId, oreBlock);
                    onOreBlockRegister(oreBlock);

                    if (oreBlockItem != null) {
                        Registry.register(Registry.ITEM, blockRegistryId, oreBlockItem);
                        onOreBlockItemRegister(oreBlockItem);
                    }

                    this.registeredOreBlocks.put(blockType, oreBlock);
                }
            }
        }
    }

    private OreBlock createOreBlock(OreBlockType blockType) {
        return blockType.getVariant().createOreBlock(blockType.getMaterial());
    }

    @Nullable
    private OreBlockItem createOreBlockItem(OreBlock oreBlock) {
        return oreBlock.getVariant().createOreBlockItem(oreBlock);
    }

    private void onOreBlockRegister(OreBlock oreBlock) {
    }

    private void onOreBlockItemRegister(OreBlockItem oreBlockItem) {
    }

    @Environment(EnvType.CLIENT)
    private void registerOreBlockClient(OreBlock oreBlock) {
        OreBlockType blockType = new OreBlockType(oreBlock.getVariant(), oreBlock.getMaterial());

        OreBlockVariantProvider.INSTANCE.registerOreBlock(oreBlock.getDefaultState(), new VisualOreBlockType(blockType));
        ColorProviderRegistry.BLOCK.register(OreBlockColorProvider.INSTANCE, oreBlock);
    }

    @Environment(EnvType.CLIENT)
    private void registerOreBlockItemClient(OreBlockItem oreBlockItem) {
        OreBlock oreBlock = oreBlockItem.getOreBlock();
        OreBlockType blockType = new OreBlockType(oreBlock.getVariant(), oreBlock.getMaterial());

        OreBlockVariantProvider.INSTANCE.registerOreBlockItem(oreBlockItem, new VisualOreBlockType(blockType));
        ColorProviderRegistry.ITEM.register(OreBlockColorProvider.INSTANCE, oreBlockItem);
    }

    @Environment(EnvType.CLIENT)
    public void registerOreBlocksClient() {
        for (Block block : this.registeredOreBlocks.values()) {
            if (block instanceof OreBlock oreBlock) {
                registerOreBlockClient(oreBlock);
            }
            if (block.asItem() instanceof OreBlockItem oreBlockItem) {
                registerOreBlockItemClient(oreBlockItem);
            }
        }
    }

    private Identifier createBlockId(Identifier materialId, Identifier variantId, String blockName) {
        String preferredNamespace = materialId.getNamespace();

        if (preferredNamespace.equals(GTValues.MODID)) {
            preferredNamespace = variantId.getNamespace();
        }

        return new Identifier(preferredNamespace, blockName);
    }
}
