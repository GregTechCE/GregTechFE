package gregtech.api.unification.forms;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.util.MaterialAmount;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MaterialForm {

    public static final Registry<MaterialForm> REGISTRY = FabricRegistryBuilder
            .createSimple(MaterialForm.class, new Identifier(GTValues.MODID, "material_forms"))
            .buildAndRegister();

    public static final String COMMON_TAG_NAMESPACE = "c";
    private static final String MATERIAL_TEMPLATE = "{material}";

    private final String tagNameTemplate;
    private final MaterialAmount materialAmount;
    private final MaterialFormMatcher matcher;

    private final Map<Material, Tag.Identified<Item>> materialItemTagCache = new HashMap<>();

    public MaterialForm(Settings settings) {
        Preconditions.checkNotNull(settings.tagNameTemplate, "tagNameTemplate not set");

        this.tagNameTemplate = settings.tagNameTemplate;
        this.materialAmount = settings.materialAmount;
        this.matcher = new MaterialFormMatcher(tagNameTemplate);
    }

    public MaterialAmount getMaterialAmount(Material material) {
        return materialAmount;
    }

    public Tag.Identified<Item> getItemTag(Material material) {
        return materialItemTagCache.computeIfAbsent(material, this::createItemTag);
    }

    protected Tag.Identified<Item> createItemTag(Material material) {
        Identifier materialId = material.getName();
        String tagName = this.tagNameTemplate.replace(MATERIAL_TEMPLATE, materialId.getPath());
        Identifier tagId = new Identifier(COMMON_TAG_NAMESPACE, tagName);

        return TagRegistry.create(tagId, ItemTags::getTagGroup);
    }

    private static Material findMaterialByName(String materialName) {
        for (Identifier materialId : Material.REGISTRY.getIds()) {
            if (materialId.getPath().equals(materialName)) {
                return Material.REGISTRY.get(materialId);
            }
        }
        return null;
    }

    @Nullable
    public static Pair<MaterialForm, Material> matchTag(Tag.Identified<?> tag) {
        Identifier tagId = tag.getId();
        if (!tagId.getNamespace().equals(COMMON_TAG_NAMESPACE)) {
            return null;
        }

        String tagPath = tagId.getPath();
        for (MaterialForm materialForm : REGISTRY.stream().toList()) {
            String matchedMaterial = materialForm.matcher.match(tagPath);
            if (matchedMaterial != null) {
                Material material = findMaterialByName(matchedMaterial);
                if (material != null) {
                    return Pair.of(materialForm, material);
                }
            }
        }
        return null;
    }

    public static class Settings {
        private String tagNameTemplate;
        private MaterialAmount materialAmount = MaterialAmount.ZERO;

        public Settings tagNameTemplate(String tagNameTemplate) {
            Preconditions.checkNotNull(tagNameTemplate);
            this.tagNameTemplate = tagNameTemplate;
            return this;
        }

        public Settings materialAmount(MaterialAmount materialAmount) {
            Preconditions.checkNotNull(materialAmount);
            this.materialAmount = materialAmount;
            return this;
        }
    }

    private static class MaterialFormMatcher {
        private final String prefix;
        private final String postfix;

        public MaterialFormMatcher(String tagNameTemplate) {
            int materialIndex = tagNameTemplate.indexOf(MATERIAL_TEMPLATE);
            if (materialIndex == -1) {
                this.prefix = tagNameTemplate;
                this.postfix = "";
            } else {
                this.prefix = tagNameTemplate.substring(0, materialIndex);
                this.postfix = tagNameTemplate.substring(materialIndex + MATERIAL_TEMPLATE.length());
            }
        }

        @Nullable
        public String match(String tagPath) {
            if (tagPath.startsWith(prefix) && tagPath.endsWith(postfix)) {
                return tagPath.substring(prefix.length(), tagPath.length() - postfix.length());
            }
            return null;
        }
    }
}
