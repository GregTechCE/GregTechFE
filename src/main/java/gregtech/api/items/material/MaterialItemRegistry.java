package gregtech.api.items.material;

import gregtech.api.GTValues;
import gregtech.api.render.RemappingModelResourceProvider;
import gregtech.api.unification.material.type.Material;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MaterialItemRegistry {

    public static final MaterialItemRegistry INSTANCE = new MaterialItemRegistry();

    private final Map<MaterialItemId, MaterialItem> registeredMaterialItems = new HashMap<>();

    private MaterialItemRegistry() {
    }

    @Nullable
    public MaterialItem getItem(MaterialItemId itemId) {
        return registeredMaterialItems.get(itemId);
    }

    public ItemStack getMaterialItem(MaterialItemId itemId, int amount) {
        MaterialItem materialItem = registeredMaterialItems.get(itemId);
        if (materialItem == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(materialItem, amount);
    }

    public ItemStack getMaterialItem(MaterialItemForm form, Material material, int amount) {
        return getMaterialItem(new MaterialItemId(form, material), amount);
    }

    private MaterialItem createMaterialItem(MaterialItemId itemId) {
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
        ModelIdentifier modelIdentifier = materialItem.getModelLocation();

        RemappingModelResourceProvider.INSTANCE.registerItemModel(materialItem, modelIdentifier);
        ColorProviderRegistry.ITEM.register(MaterialItemColorProvider.INSTANCE, materialItem);
    }

    public void registerMaterialItems() {
        for (Identifier materialId : Material.REGISTRY.getIds()) {
            Material material = Material.REGISTRY.get(materialId);

            for (Identifier formId : MaterialItemForm.REGISTRY.getIds()) {
                MaterialItemForm itemForm = MaterialItemForm.REGISTRY.get(formId);

                //noinspection ConstantConditions
                if (itemForm.shouldGenerateFor(material)) {
                    MaterialItemId materialItemId = new MaterialItemId(itemForm, material);
                    MaterialItem materialItem = createMaterialItem(materialItemId);

                    Identifier itemRegistryId = createItemId(formId, materialId);
                    Registry.register(Registry.ITEM, itemRegistryId, materialItem);

                    onMaterialItemRegister(materialItem);
                    this.registeredMaterialItems.put(materialItemId, materialItem);
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void registerMaterialItemsClient() {
        for (MaterialItem materialItem : this.registeredMaterialItems.values()) {
            registerMaterialItemClient(materialItem);
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
