package gregtech.api.item;

import alexiil.mc.lib.attributes.AttributeProviderItem;
import alexiil.mc.lib.attributes.ItemAttributeList;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.GroupedFluidInv;
import alexiil.mc.lib.attributes.fluid.GroupedFluidInvView;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.NullVariant;
import alexiil.mc.lib.attributes.misc.PlayerInvUtil;
import alexiil.mc.lib.attributes.misc.Reference;
import gregtech.api.capability.item.DischargeMode;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.item.TransferLimit;
import gregtech.api.item.stats.ElectricStats;
import gregtech.api.item.stats.FluidStats;
import gregtech.api.item.util.CustomMaxCountItem;
import gregtech.api.util.ElectricItemUtil;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ref.InventorySlotRef;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class GTItem extends Item implements AttributeProviderItem, CustomMaxCountItem {

    private final FluidStats fluidStats;
    private final ElectricStats electricStats;

    public GTItem(GTItemSettings settings) {
        super(settings);
        this.fluidStats = settings.fluidStats;
        this.electricStats = settings.electricStats;
    }

    public boolean isElectricItem() {
        return this.electricStats != null;
    }

    public boolean isFluidContainer() {
        return this.fluidStats != null;
    }

    @Override
    public Text getName(ItemStack stack) {
        String translationKey = getTranslationKey(stack);

        if (isFluidContainer()) {
            GroupedFluidInvView fluidInv = FluidAttributes.GROUPED_INV_VIEW.get(stack);
            Set<FluidKey> fluidKeys = fluidInv.getStoredFluids();
            FluidKey storedFluid = fluidKeys.isEmpty() ? FluidKeys.EMPTY : fluidKeys.iterator().next();

            return new TranslatableText(translationKey, storedFluid.name);
        }

        return new TranslatableText(translationKey);
    }

    @Override
    public int getMaxCount(ItemStack itemStack) {
        if (isElectricItem()) {
            ElectricItem electricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(itemStack);
            if (electricItem != null && electricItem.getCharge() > 0L) {
                return 1;
            }
        }
        return this.getMaxCount();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (isElectricItem()) {
            ElectricItem electricItem = GTAttributes.ELECTRIC_ITEM.getFirstOrNull(stack);

            if (electricItem != null) {
                electricItem.addItemTooltip(tooltip, context);
            }
        }

        if (isFluidContainer()) {
            GroupedFluidInvView fluidInv = FluidAttributes.GROUPED_INV_VIEW.get(stack);
            if (!(fluidInv instanceof NullVariant)) {
                addFluidContainerTooltip(fluidInv, tooltip);
            }
        }

        if (isElectricItem()) {
            if (electricStats.canProvideChargeExternally) {
                tooltip.add(new TranslatableText("item.gregtech.electric.discharge_mode.tooltip"));

                if (isInDishargeMode(stack)) {
                    tooltip.add(new TranslatableText("item.gregtech.electric.discharge_mode.active"));
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (isElectricItem()) {
            if (electricStats.canProvideChargeExternally && user.isSneaking()) {
                if (!world.isClient()) {
                    cycleDischargeMode(itemStack, user);
                }
                return TypedActionResult.success(itemStack);
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void addAllAttributes(Reference<ItemStack> stack, LimitedConsumer<ItemStack> excess, ItemAttributeList<?> to) {
        if (isElectricItem()) {
            ElectricItem electricItem = electricStats.createImplementation(stack, excess);
            to.offer(electricItem);
        }

        if (isFluidContainer()) {
            GroupedFluidInv fluidInventory = fluidStats.createImplementation(stack, excess);
            to.offer(fluidInventory);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (isElectricItem()) {
            if (electricStats.canProvideChargeExternally && isInDishargeMode(stack)) {
                if (!world.isClient() && entity instanceof PlayerEntity player) {

                    PlayerInventory playerInventory = player.getInventory();
                    int realSlot = GTUtility.getItemStackInventorySlot(playerInventory, stack);

                    ElectricItem electricItem = getItemFromSlot(player, playerInventory, realSlot);

                    if (electricItem != null && electricItem.getCharge() > 0) {
                        chargeInventoryItems(player, playerInventory, electricItem);
                    }
                }
            }
        }
    }

    private void addFluidContainerTooltip(GroupedFluidInvView fluidInv, List<Text> tooltip) {
        Set<FluidKey> storedFluids = fluidInv.getStoredFluids();

        if (storedFluids.isEmpty()) {
            FluidAmount totalCapacity = fluidInv.getTotalCapacity_F();
            Text emptyTooltipLine = FluidKeys.EMPTY.unitSet.getEmptyTank(totalCapacity);
            tooltip.add(emptyTooltipLine);

        } else {
            for (FluidKey storedFluid : storedFluids) {
                GroupedFluidInvView.FluidInvStatistic statistic = fluidInv.getStatistics(storedFluid);

                Text fluidDisplayName = storedFluid.name;
                Text tooltipLine = storedFluid.unitSet.getPartialTank(statistic.amount_F, statistic.spaceTotal_F, fluidDisplayName);
                tooltip.add(tooltipLine);
            }
        }
    }

    private ElectricItem getItemFromSlot(PlayerEntity player, Inventory inventory, int slot) {
        Reference<ItemStack> stackRef = InventorySlotRef.of(inventory, slot);
        Consumer<ItemStack> excess = PlayerInvUtil.createPlayerInsertable(player);

        return GTAttributes.ELECTRIC_ITEM.getFirstOrNull(stackRef, LimitedConsumer.fromConsumer(excess));
    }

    private void chargeInventoryItems(PlayerEntity player, Inventory inventory, ElectricItem source) {
        for (int i = 0; i < inventory.size(); i++) {
            ElectricItem targetItem = getItemFromSlot(player, inventory, i);

            if (targetItem != null && !targetItem.canProvideChargeExternally()) {
                if (ElectricItemUtil.chargeElectricItem(source, targetItem, DischargeMode.EXTERNAL, TransferLimit.RESPECT) == 0L) {
                    return;
                }
            }
        }
    }

    private void cycleDischargeMode(ItemStack itemStack, PlayerEntity user) {
        boolean newDischargeMode = !isInDishargeMode(itemStack);

        Text resultMessage;
        if (newDischargeMode) {
            resultMessage = new TranslatableText("item.gregtech.electric.discharge_mode.enabled");
        } else {
            resultMessage = new TranslatableText("item.gregtech.electric.discharge_mode.disabled");
        }
        user.sendMessage(resultMessage, true);
        setInDischargeMode(itemStack, newDischargeMode);
    }

    private static void setInDischargeMode(ItemStack itemStack, boolean isDischargeMode) {
        if (isDischargeMode) {
            NbtCompound compound = itemStack.getOrCreateTag();
            compound.putBoolean("DischargeMode", true);
        } else {
            NbtCompound tagCompound = itemStack.getTag();
            if (tagCompound != null) {
                tagCompound.remove("DischargeMode");
                if (tagCompound.isEmpty()) {
                    itemStack.setTag(null);
                }
            }
        }
    }
    private static boolean isInDishargeMode(ItemStack itemStack) {
        NbtCompound compound = itemStack.getTag();
        return compound != null && compound.getBoolean("DischargeMode");
    }
}
