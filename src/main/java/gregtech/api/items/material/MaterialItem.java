package gregtech.api.items.material;

import gregtech.api.damagesources.GTDamageSource;
import gregtech.api.items.util.AutoTaggedItem;
import gregtech.api.items.util.ItemEntityAwareItem;
import gregtech.api.unification.material.properties.MaterialProperties;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.MaterialAmount;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class MaterialItem extends Item implements ItemEntityAwareItem, AutoTaggedItem {

    private static final float RADIATION_DAMAGE_PER_SECOND = 1.5f;
    private static final float HEAT_DAMAGE_PER_SECOND = 2.0f;

    private final MaterialItemForm itemForm;
    private final Material material;

    public MaterialItem(Settings settings, MaterialItemForm itemForm, Material material) {
        super(settings);
        this.itemForm = itemForm;
        this.material = material;
    }

    public MaterialItemForm getItemForm() {
        return itemForm;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public Text getName() {
        return this.itemForm.getDisplayName(this.material);
    }

    @Override
    public Text getName(ItemStack stack) {
        return this.itemForm.getDisplayName(this.material);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.getTime() % 20 == 0L) {
            if (this.material.isRadioactive()) {
                float damageMultiplier = getDamageMultiplier(stack);
                entity.damage(GTDamageSource.RADIATION, damageMultiplier * RADIATION_DAMAGE_PER_SECOND);
            }
            if (this.itemForm.hasHeatDamage()) {
                float damageMultiplier = getDamageMultiplier(stack);
                entity.damage(GTDamageSource.HEAT, damageMultiplier * HEAT_DAMAGE_PER_SECOND);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (this.material.isRadioactive()) {
            tooltip.add(new TranslatableText("item.gregtech.material.tooltip.radioactive"));
        }
        if (this.itemForm.hasHeatDamage()) {
            tooltip.add(new TranslatableText("item.gregtech.material.tooltip.hot"));
        }
        if (this.itemForm.canBePurified()) {
            tooltip.add(new TranslatableText("item.gregtech.material.tooltip.purify"));
        }
    }

    @Override
    public void onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity) {
        if (this.itemForm.canBePurified()) {
            TypedActionResult<ItemStack> purificationResult = tryPurifyItem(stack, itemEntity.world, itemEntity.getBlockPos());

            if (purificationResult.getResult().isAccepted()) {
                itemEntity.setStack(purificationResult.getValue());
            }
        }
    }

    @Override
    public void addItemTags(Set<Tag.Identified<Item>> outTags) {
        this.itemForm.addTagsForMaterial(material, outTags);

        if (this.material instanceof DustMaterial dustMaterial) {
            if (dustMaterial.harvestLevel >= 2 && this.itemForm.canBeUsedAsBeaconPayment()) {
                outTags.add(ItemTags.BEACON_PAYMENT_ITEMS);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    ModelIdentifier getModelLocation() {
        Identifier itemFormName = this.itemForm.getName();
        String modelType = itemFormName.getNamespace() + "/" + itemFormName.getPath();

        Identifier modelId = this.material.materialIconSet.getModelLocation(modelType);
        return new ModelIdentifier(modelId, "inventory");
    }

    float getDamageMultiplier(ItemStack itemStack) {
        MaterialAmount formAmount = this.itemForm.getMaterialAmount();
        float damageMultiplier = MathHelper.clamp((float) formAmount.div(MaterialAmount.DUST), 0.1f, 2.0f);
        float stackSizeMultiplier = MathHelper.clamp(itemStack.getCount() / 20.0f, 1.0f, 2.0f);

        return damageMultiplier * stackSizeMultiplier;
    }

    int getItemBurnTime() {
        MaterialAmount formAmount = this.itemForm.getMaterialAmount();
        int oneDustBurnTime = material.queryProperty(MaterialProperties.BURN_TIME).orElse(0);
        return formAmount.mul(oneDustBurnTime).divFloor(MaterialAmount.DUST);
    }

    TypedActionResult<ItemStack> tryPurifyItem(ItemStack stack, World world, BlockPos blockPos) {
        if (!this.itemForm.canBePurified()) {
            return TypedActionResult.pass(stack);
        }

        MaterialItemForm purifiedInto = this.itemForm.getPurifiedInto();
        BlockState blockState = world.getBlockState(blockPos);

        if (blockState.isOf(Blocks.WATER_CAULDRON)) {
            int waterLevel = blockState.get(LeveledCauldronBlock.LEVEL);

            if (waterLevel >= 1) {
                BlockState newBlockState = blockState.with(LeveledCauldronBlock.LEVEL, waterLevel - 1);
                world.setBlockState(blockPos, newBlockState);

                ItemStack purifiedItemStack = MaterialItemRegistry.INSTANCE
                        .getMaterialItem(purifiedInto, this.material, stack.getCount());
                return TypedActionResult.success(purifiedItemStack);
            }
            return TypedActionResult.fail(stack);
        }
        return TypedActionResult.pass(stack);
    }
}
