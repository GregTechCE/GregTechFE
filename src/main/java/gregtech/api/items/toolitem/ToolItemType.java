package gregtech.api.items.toolitem;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import gregtech.api.unification.material.properties.MaterialProperties;
import gregtech.api.unification.material.Material;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ToolItemType {

    public static final Registry<ToolItemType> REGISTRY = FabricRegistryBuilder
            .createSimple(ToolItemType.class, new Identifier(GTValues.MODID, "tool_item_type"))
            .buildAndRegister();

    private final ToolItemSettings settings;
    private final ToolItemMaker toolItemMaker;
    private Tag.Identified<Item> toolTypeTag;

    public ToolItemType(ToolItemSettings settings, ToolItemMaker toolItemMaker) {
        this.settings = settings;
        this.toolItemMaker = toolItemMaker;
    }

    public ToolItem createItem(Material material) {
        return this.toolItemMaker.create(settings, this, material);
    }

    public Identifier getName() {
        return REGISTRY.getId(this);
    }

    public boolean shouldGenerateFor(Material material) {
        return material.queryProperty(MaterialProperties.TOOL_PROPERTIES).isPresent();
    }

    public Tag.Identified<Item> getToolTypeTag() {
        if (toolTypeTag == null) {
            Identifier toolItemTypeId = Preconditions.checkNotNull(getName());
            String toolTagPath = "tool/" + toolItemTypeId.getPath();

            Identifier tagId = new Identifier(toolItemTypeId.getNamespace(), toolTagPath);
            this.toolTypeTag = (Tag.Identified<Item>) TagRegistry.item(tagId);
        }
        return toolTypeTag;
    }

    @FunctionalInterface
    public interface ToolItemMaker {
        ToolItem create(ToolItemSettings settings, ToolItemType itemType, Material material);
    }
}
