package gregtech.api.items.toolitem;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ToolItemRegistry {

    public static final ToolItemRegistry INSTANCE = new ToolItemRegistry();

    private final Map<ToolItemId, ToolItem> registeredToolItems = new HashMap<>();

    private ToolItemRegistry() {
    }

    @Nullable
    public ToolItem getItem(ToolItemId toolItemId) {
        return registeredToolItems.get(toolItemId);
    }

    @Nullable
    public ToolItem getItem(ToolItemType toolItemType, Material material) {
        return getItem(new ToolItemId(toolItemType, material));
    }

    private ToolItem createToolItem(ToolItemId toolItemId) {
        return toolItemId.getItemType().createItem(toolItemId.getMaterial());
    }

    private void onToolItemRegister(ToolItem toolItem) {
    }

    @Environment(EnvType.CLIENT)
    private void registerToolItemClient(ToolItem toolItem) {
        ColorProviderRegistry.ITEM.register(ToolItemColorProvider.INSTANCE, toolItem);
    }

    public void registerToolItems() {
        for (Identifier materialId : Material.REGISTRY.getIds()) {
            Material material = Material.REGISTRY.get(materialId);

            for (Identifier toolTypeId : ToolItemType.REGISTRY.getIds()) {
                ToolItemType toolItemType = ToolItemType.REGISTRY.get(toolTypeId);

                //noinspection ConstantConditions
                if (toolItemType.shouldGenerateFor(material)) {
                    ToolItemId toolItemId = new ToolItemId(toolItemType, material);
                    ToolItem toolItem = createToolItem(toolItemId);

                    Identifier itemRegistryId = createItemId(toolTypeId, materialId);
                    Registry.register(Registry.ITEM, itemRegistryId, toolItem);

                    onToolItemRegister(toolItem);
                    this.registeredToolItems.put(toolItemId, toolItem);
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void registerToolItemsClient() {
        for (ToolItem toolItem : this.registeredToolItems.values()) {
            registerToolItemClient(toolItem);
        }
    }

    private Identifier createItemId(Identifier toolTypeId, Identifier materialId) {
        //Prefer non-GT namespaces and prioritize material namespace,
        //since addons usually add materials and not tool types
        String preferredNamespace = materialId.getNamespace();

        if (preferredNamespace.equals(GTValues.MODID)) {
            preferredNamespace = toolTypeId.getNamespace();
        }

        //Designed after Vanilla names, so for ToolItemTypes.PICKAXE and Material.IRON we get something like iron_pickaxe
        String itemPath = materialId.getPath() + "_" + toolTypeId.getPath();
        return new Identifier(preferredNamespace, itemPath);
    }
}
