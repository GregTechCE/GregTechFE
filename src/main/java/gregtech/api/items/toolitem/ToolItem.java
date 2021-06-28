package gregtech.api.items.toolitem;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.PlayerInvUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.item.CustomDamageItem;
import gregtech.api.enchants.EnchantmentData;
import gregtech.api.items.GTItem;
import gregtech.api.items.GTItemSettings;
import gregtech.api.items.util.AutoTaggedItem;
import gregtech.api.items.util.CustomEnchantableItem;
import gregtech.api.items.util.ExtendedRemainderItem;
import gregtech.api.unification.material.forms.MaterialForm;
import gregtech.api.unification.material.properties.MaterialProperties;
import gregtech.api.unification.material.properties.SolidForm;
import gregtech.api.unification.material.properties.ToolProperties;
import gregtech.api.unification.material.type.Material;
import gregtech.api.util.GTUtility;
import gregtech.api.util.SimpleReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class ToolItem extends GTItem implements CustomDamageItem, CustomEnchantableItem, Vanishable, DynamicAttributeTool, AutoTaggedItem, ExtendedRemainderItem {

    protected final ToolItemType toolItemType;
    protected final Material material;

    private final float miningSpeedMultiplier;
    private final float attackSpeed;
    private final float baseAttackDamage;
    private final float attackDamageMultiplier;

    protected final int damagePerSpecialAction;
    private final int damagePerBlockBreak;
    private final int damagePerEntityAttack;
    private final int damagePerCraft;

    private final long energyPerDurabilityPoint;
    private final int itemDamageChance;

    private final String translationKey;

    public ToolItem(ToolItemSettings settings, ToolItemType toolItemType, Material material) {
        super((GTItemSettings) settings.maxDamageIfAbsent(getMaxDurability(settings, material)));
        this.toolItemType = toolItemType;
        this.material = material;

        this.miningSpeedMultiplier = settings.miningSpeedMultiplier;
        this.attackSpeed = settings.attackSpeed;
        this.baseAttackDamage = settings.baseAttackDamage;
        this.attackDamageMultiplier = settings.attackDamageMultiplier;

        this.damagePerSpecialAction = settings.damagePerSpecialAction;
        this.damagePerBlockBreak = settings.damagePerBlockBreak;
        this.damagePerEntityAttack = settings.damagePerEntityAttack;
        this.damagePerCraft = settings.damagePerCraft;

        this.energyPerDurabilityPoint = settings.energyPerDurabilityPoint;
        this.itemDamageChance = settings.itemDamageChance;

        this.translationKey = Util.createTranslationKey("item", toolItemType.getName());
    }

    private static int getMaxDurability(ToolItemSettings settings, Material material) {
        int materialDurability = material.queryProperty(MaterialProperties.TOOL_PROPERTIES)
                .map(ToolProperties::getDurability)
                .orElse(1);
        return Math.round(materialDurability * settings.durabilityMultiplier);
    }

    public ToolItemType getToolItemType() {
        return toolItemType;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    protected String getOrCreateTranslationKey() {
        return translationKey;
    }

    @Override
    public Text getName(ItemStack stack) {
        return new TranslatableText(getTranslationKey(), this.material.getDisplayName());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.isDamageable()) {
            int maxDamage = stack.getMaxDamage();
            int damage = stack.getDamage();
            tooltip.add(new TranslatableText("item.gregtech.tool.durability", maxDamage - damage, maxDamage));
        }

        tooltip.add(new TranslatableText("item.gregtech.tool.material", material.getDisplayName()));

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Environment(EnvType.CLIENT)
    ModelIdentifier getModelLocation() {
        Identifier toolTypeName = this.toolItemType.getName();
        String modelPath = "tools/" + toolTypeName.getPath();

        Identifier modelId  = new Identifier(toolTypeName.getNamespace(), modelPath);
        return new ModelIdentifier(modelId, "inventory");
    }

    /**
     * Returns a tag used to determine tools effective blocks
     * Valid values are listed in FabricToolTags and pretty much represent vanilla tool types
     *
     * @see net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
     * @return tag of the tool class this item has
     */
    protected abstract Tag.Identified<Item> getToolClassTag();

    @Override
    public void addItemTags(Set<Tag.Identified<Item>> outTags) {
        outTags.add(this.toolItemType.getToolTypeTag());
        outTags.add(getToolClassTag());
    }

    protected ItemStack getBrokenItemRemainder(ItemStack stack) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUseInRecipe(ItemStack stack) {
        return attemptDamageItem(stack, this.damagePerCraft, null, Simulation.SIMULATE).getLeft().isSuccess();
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return attemptDamageItem(stack, this.damagePerCraft, null, Simulation.ACTION).getRight();
    }

    protected void addBuiltinEnchantments(List<EnchantmentData> outEnchantments) {
        this.material.queryProperty(MaterialProperties.TOOL_PROPERTIES)
                .map(ToolProperties::getEnchantments)
                .ifPresent(outEnchantments::addAll);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        List<EnchantmentData> enchantments = new ArrayList<>();
        addBuiltinEnchantments(enchantments);

        Map<Enchantment, Integer> enchantmentMap = EnchantmentData.reduceEnchantmentList(enchantments);
        if (!enchantmentMap.isEmpty()) {
            EnchantmentHelper.set(enchantmentMap, stack);
        }
    }

    @Override
    public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, @Nullable LivingEntity user) {
        return this.material.queryPropertyChecked(MaterialProperties.HARVEST_LEVEL);
    }

    @Override
    public float getMiningSpeedMultiplier(Tag<Item> tag, BlockState state, ItemStack stack, @Nullable LivingEntity user) {
        if (canDamageItem(stack, damagePerBlockBreak, null)) {
            float materialSpeed = this.material.queryProperty(MaterialProperties.TOOL_PROPERTIES)
                    .map(ToolProperties::getMiningSpeed)
                    .orElse(1.0f);
            return materialSpeed * this.miningSpeedMultiplier;
        }
        return 1.0f;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getDynamicModifiers(EquipmentSlot slot, ItemStack stack, @Nullable LivingEntity user) {
        if (canDamageItem(stack, damagePerEntityAttack, null)) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

            float materialAttackDamage = this.material.queryProperty(MaterialProperties.TOOL_PROPERTIES)
                    .map(ToolProperties::getAttackDamage)
                    .orElse(0.0f);
            float attackDamage = this.baseAttackDamage + this.attackDamageMultiplier * materialAttackDamage;

            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier",
                    attackDamage, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier",
                    this.attackSpeed, EntityAttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return ImmutableMultimap.of();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantability() {
        Optional<ToolProperties> toolProperties = this.material.queryProperty(MaterialProperties.TOOL_PROPERTIES);
        return toolProperties
                .map(ToolProperties::getEnchantability)
                .orElse(0);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        Optional<SolidForm> solidForm = this.material.queryProperty(MaterialProperties.SOLID_FORM);
        if (solidForm.isPresent()) {
            MaterialForm materialForm = solidForm.get().getMaterialForm();;
            return materialForm.getItemTag(material).contains(ingredient.getItem());
        }
        return false;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        ElectricItem electricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(stack);
        return super.isItemBarVisible(stack) || electricItem != null;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        ElectricItem electricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(stack);
        if (electricItem != null) {
            float chargeAmount = electricItem.getCharge() / (electricItem.getMaxCharge() * 1.0f);
            return Math.round(13.0f - chargeAmount * 13.0f);
        }
        return super.getItemBarStep(stack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return CustomDamageItem.damageItem(attacker, Hand.MAIN_HAND, damagePerEntityAttack, Simulation.ACTION);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return CustomDamageItem.damageItem(miner, Hand.MAIN_HAND, damagePerBlockBreak, Simulation.ACTION);
    }

    @Override
    public Pair<ItemDamageResult, ItemStack> attemptDamageItem(ItemStack itemStack, int damage, @Nullable LivingEntity entity, Simulation simulate) {
        SimpleReference<ItemStack> stackRef = new SimpleReference<>(itemStack.copy());
        LimitedConsumer<ItemStack> excess = LimitedConsumer.rejecting();
        Random random = new Random();

        if (entity instanceof PlayerEntity playerEntity) {
            excess = LimitedConsumer.fromConsumer(PlayerInvUtil.createPlayerInsertable(playerEntity));
        }

        ElectricItem electricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(stackRef, excess);

        if (electricItem != null) {
            long energyToUse = energyPerDurabilityPoint * damage;

            if (!electricItem.canUse(energyToUse)) {
                return Pair.of(ItemDamageResult.CANNOT_DAMAGE, stackRef.value);
            }
            if (simulate.isAction()) {
                electricItem.use(energyToUse);
            }

            for (int i = 0; i < damage; i++) {
                if (random.nextInt(itemDamageChance) != 0)
                    damage--;
            }
        }

        if (simulate.isAction()) {
            if (stackRef.value.damage(damage, random, null)) {
                stackRef.value.decrement(1);

                if (stackRef.value.isEmpty()) {
                    stackRef.set(getBrokenItemRemainder(itemStack));
                }
                return Pair.of(ItemDamageResult.BROKEN, stackRef.value);
            }
        }

        return Pair.of(ItemDamageResult.DAMAGED, stackRef.value);
    }
}
