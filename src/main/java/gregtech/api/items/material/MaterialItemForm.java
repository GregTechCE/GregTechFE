package gregtech.api.items.material;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import gregtech.api.GTCreativeTabs;
import gregtech.api.GTValues;
import gregtech.api.unification.forms.MaterialForm;
import gregtech.api.unification.material.flags.MaterialFlag;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.util.MaterialAmount;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MaterialItemForm {

    public static final Registry<MaterialItemForm> REGISTRY = FabricRegistryBuilder
            .createSimple(MaterialItemForm.class, new Identifier(GTValues.MODID, "material_forms"))
            .buildAndRegister();

    private final List<MaterialFlag> requiredMaterialFlags;
    private final MaterialAmount materialAmount;
    private final int maxStackSize;
    private final Set<MaterialForm> tags;
    private final Map<Material, Item> builtinItems;
    private final MaterialItemForm purifiedInto;
    private final boolean dealsHeatDamage;
    private final boolean canBeUsedAsBeaconPayment;
    private final boolean canBeBurned;

    public MaterialItemForm(Settings settings) {
        this.requiredMaterialFlags = settings.requiredMaterialFlags;
        this.materialAmount = settings.materialAmount;
        this.maxStackSize = settings.maxCount;
        this.tags = settings.tags;
        this.builtinItems = ImmutableMap.copyOf(settings.builtinItems);
        this.purifiedInto = settings.purifiedInto;
        this.dealsHeatDamage = settings.dealsHeatDamage;
        this.canBeUsedAsBeaconPayment = settings.canBeUsedAsBeaconPayment;
        this.canBeBurned = settings.canBeBurned;
    }

    public void addTagsForMaterial(Material material, Set<Tag.Identified<Item>> outTags) {
        for (MaterialForm materialForm : this.tags) {
            outTags.add(materialForm.getItemTag(material));
        }
    }

    public MaterialAmount getMaterialAmount() {
        return materialAmount;
    }

    public boolean canBeUsedAsBeaconPayment() {
        return canBeUsedAsBeaconPayment;
    }

    public boolean canBePurified() {
        return this.purifiedInto != null;
    }

    @Nullable
    public MaterialItemForm getPurifiedInto() {
        return purifiedInto;
    }

    public boolean hasHeatDamage() {
        return dealsHeatDamage;
    }

    public boolean canBeBurned() {
        return canBeBurned;
    }

    protected FabricItemSettings createItemSettings(Material material) {
        return new FabricItemSettings()
                .maxCount(maxStackSize)
                .group(GTCreativeTabs.MATERIALS);
    }

    public MaterialItem createItem(Material material) {
        return new MaterialItem(createItemSettings(material), this, material);
    }

    @Nullable
    public Item getBuiltinItemFor(Material material) {
        return builtinItems.get(material);
    }

    public boolean shouldGenerateFor(Material material) {
        for (MaterialFlag flag : this.requiredMaterialFlags) {
            if (!material.hasFlag(flag)) {
                return false;
            }
        }
        return true;
    }

    private String getTranslationKey() {
        Identifier id = getName();
        return "material_form." + id.getNamespace() + "." + id.getPath();
    }

    public Text getDisplayName(Material material) {
        Text materialName = material.getDisplayName();
        return new TranslatableText(getTranslationKey(), materialName);
    }

    public Identifier getName() {
        return Preconditions.checkNotNull(REGISTRY.getId(this));
    }

    public static class Settings {
        List<MaterialFlag> requiredMaterialFlags;
        MaterialAmount materialAmount = MaterialAmount.ZERO;
        int maxCount = Inventory.MAX_COUNT_PER_STACK;
        Map<Material, Item> builtinItems = new HashMap<>();

        Set<MaterialForm> tags = new HashSet<>();
        MaterialItemForm purifiedInto;
        boolean dealsHeatDamage;
        boolean canBeUsedAsBeaconPayment;
        boolean canBeBurned;

        public Settings requiredMaterialFlags(MaterialFlag... materialFlags) {
            this.requiredMaterialFlags = Arrays.asList(materialFlags);
            return this;
        }

        public Settings materialAmount(MaterialAmount materialAmount) {
            this.materialAmount = materialAmount;
            return this;
        }

        public Settings maxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Settings addBuiltinItem(Material material, Item builtinItem) {
            this.builtinItems.put(material, builtinItem);
            return this;
        }

        public Settings tag(MaterialForm tag) {
            this.tags.add(tag);
            return this;
        }

        public Settings purifiedInto(MaterialItemForm purifiedInto) {
            this.purifiedInto = purifiedInto;
            return this;
        }

        public Settings dealsHeatDamage() {
            this.dealsHeatDamage = true;
            return this;
        }

        public Settings canBeUsedAsBeaconPayment() {
            this.canBeUsedAsBeaconPayment = true;
            return this;
        }

        public Settings canBeBurned() {
            this.canBeBurned = true;
            return this;
        }
    }
}
