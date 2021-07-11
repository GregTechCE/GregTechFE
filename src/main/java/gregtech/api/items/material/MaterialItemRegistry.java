package gregtech.api.items.material;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MaterialItemRegistry {

    public static final MaterialItemRegistry INSTANCE = new MaterialItemRegistry();

    private final Map<MaterialItemId, Item> registeredMaterialItems = new HashMap<>();

    private MaterialItemRegistry() {
    }

    @NotNull
    public Item getMaterialItem(MaterialItemId itemId) {
        return registeredMaterialItems.getOrDefault(itemId, Items.AIR);
    }

    @NotNull
    public Item getMaterialItem(MaterialItemForm itemForm, Material material) {
        return getMaterialItem(new MaterialItemId(itemForm, material));
    }

    public ItemStack getMaterialItem(MaterialItemId itemId, int amount) {
        Item materialItem = registeredMaterialItems.get(itemId);
        return new ItemStack(materialItem, amount);
    }

    public ItemStack getMaterialItem(MaterialItemForm form, Material material, int amount) {
        return getMaterialItem(new MaterialItemId(form, material), amount);
    }

    private Item createMaterialItem(MaterialItemId itemId) {
        return itemId.getForm().createItem(itemId.getMaterial());
    }

    private void onMaterialItemRegister(MaterialItem materialItem) {
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(
                materialItem, CauldronMaterialItemBehavior.INSTANCE);

        int fuelBurnTime = materialItem.getItemBurnTime();
        if (fuelBurnTime > 0) {
            FuelRegistry.INSTANCE.add(materialItem, fuelBurnTime);
        }
    }

    @Environment(EnvType.CLIENT)
    private void registerMaterialItemClient(MaterialItem materialItem) {
        ColorProviderRegistry.ITEM.register(MaterialItemColorProvider.INSTANCE, materialItem);
    }

    public void registerMaterialItems() {
        for (Identifier materialId : Material.REGISTRY.getIds()) {
            Material material = Material.REGISTRY.get(materialId);

            for (Identifier formId : MaterialItemForm.REGISTRY.getIds()) {
                MaterialItemForm itemForm = MaterialItemForm.REGISTRY.get(formId);

                //noinspection ConstantConditions
                Item builtinItem = itemForm.getBuiltinItemFor(material);
                if (builtinItem != null) {
                    MaterialItemId materialItemId = new MaterialItemId(itemForm, material);
                    this.registeredMaterialItems.put(materialItemId, builtinItem);
                    continue;
                }

                if (itemForm.shouldGenerateFor(material)) {
                    MaterialItemId materialItemId = new MaterialItemId(itemForm, material);
                    Item item = createMaterialItem(materialItemId);

                    Identifier itemRegistryId = createItemId(formId, materialId);
                    Registry.register(Registry.ITEM, itemRegistryId, item);

                    if (item instanceof MaterialItem materialItem) {
                        onMaterialItemRegister(materialItem);
                    }
                    this.registeredMaterialItems.put(materialItemId, item);
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void registerMaterialItemsClient() {
        for (Item item : this.registeredMaterialItems.values()) {
            if (item instanceof MaterialItem materialItem) {
                registerMaterialItemClient(materialItem);
            }
        }
    }

    private Identifier createItemId(Identifier formId, Identifier materialId) {
        //Prefer non-GT namespaces first so materials or prefixes registered by addons will
        //always have them as their registrars and not GTCE itself
        String preferredNamespace = formId.getNamespace();

        if (preferredNamespace.equals(GTValues.MODID)) {
            preferredNamespace = materialId.getNamespace();
        }

        //Designed after Vanilla names, so for MaterialForms.INGOT and Material.IRON we get something like iron_ingot
        String itemPath = materialId.getPath() + "_" + formId.getPath();
        return new Identifier(preferredNamespace, itemPath);
    }
}
