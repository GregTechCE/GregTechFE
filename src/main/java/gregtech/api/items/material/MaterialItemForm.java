package gregtech.api.items.material;

import com.google.common.base.Preconditions;
import gregtech.api.GTCreativeTabs;
import gregtech.api.GTValues;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.ore.MaterialForm;
import gregtech.api.unification.stack.MaterialAmount;
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

import java.util.HashSet;
import java.util.Set;

public class MaterialItemForm {

    public static final Registry<MaterialItemForm> REGISTRY = FabricRegistryBuilder
            .createSimple(MaterialItemForm.class, new Identifier(GTValues.MODID, "material_forms"))
            .buildAndRegister();

    private final long requiredMaterialFlags;
    private final MaterialAmount materialAmount;
    private final int maxStackSize;
    private final Set<MaterialForm> tags;
    private final Set<Material> ignoredMaterials;
    private final MaterialItemForm purifiedInto;
    private final boolean dealsHeatDamage;
    private final boolean canBeUsedAsBeaconPayment;

    public MaterialItemForm(Settings settings) {
        this.requiredMaterialFlags = settings.requiredMaterialFlags;
        this.materialAmount = settings.materialAmount;
        this.maxStackSize = settings.maxCount;
        this.tags = settings.tags;
        this.ignoredMaterials = settings.ignoredMaterials;
        this.purifiedInto = settings.purifiedInto;
        this.dealsHeatDamage = settings.dealsHeatDamage;
        this.canBeUsedAsBeaconPayment = settings.canBeUsedAsBeaconPayment;
    }

    public void addTagsForMaterial(Material material, Set<Tag.Identified<Item>> outTags) {
        for (MaterialForm materialForm : this.tags) {
            outTags.add(materialForm.getItemTagForMaterial(material));
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

    protected FabricItemSettings createItemSettings(Material material) {
        return new FabricItemSettings()
                .maxCount(maxStackSize)
                .group(GTCreativeTabs.MATERIALS);
    }

    public MaterialItem createItem(Material material) {
        return new MaterialItem(createItemSettings(material), this, material);
    }

    public boolean shouldGenerateFor(Material material) {
        return material.hasFlag(requiredMaterialFlags) &&
                !ignoredMaterials.contains(material);
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
        long requiredMaterialFlags;
        MaterialAmount materialAmount = MaterialAmount.ZERO;
        int maxCount = Inventory.MAX_COUNT_PER_STACK;
        Set<Material> ignoredMaterials = new HashSet<>();

        Set<MaterialForm> tags = new HashSet<>();
        MaterialItemForm purifiedInto;
        boolean dealsHeatDamage;
        boolean canBeUsedAsBeaconPayment;

        public Settings requiredMaterialFlags(long requiredMaterialFlags) {
            this.requiredMaterialFlags = requiredMaterialFlags;
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

        public Settings ignoreMaterial(Material material) {
            this.ignoredMaterials.add(material);
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
    }
}
