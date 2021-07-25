package gregtech.api.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import gregtech.api.capability.GTAttributes;
import gregtech.api.capability.item.ElectricItem;
import gregtech.api.capability.internal.IMultipleTankHandler;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.impl.ModularUIContainer;
import gregtech.api.item.IToolItem;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.common.ConfigHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.potion.PotionEffect;
import net.minecraft.screen.slot.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static gregtech.api.GTValues.V;

public class GTUtility {

    //Basically, slot argument provided in Item#inventoryTick is a LOCAL slot index inside
    //of the multiple player inventory components (main, armor, offhand).
    //to determine absolute index we use the fact that stack in the slot is strictly == to the stack
    //passed in inventoryTick, and we should never get the same item stack objects in multiple slots at once
    public static int getItemStackInventorySlot(Inventory inventory, ItemStack itemStack) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i) == itemStack) {
                return i;
            }
        }
        throw new IllegalArgumentException("ItemStack is not found in any of the inventory slots");
    }

    /**
     * Attempts to merge given ItemStack with ItemStacks in slot list supplied
     * If it's not possible to merge it fully, it will attempt to insert it into first empty slots
     *
     * @param itemStack item stack to merge. It WILL be modified.
     * @param simulate if true, stack won't actually modify items in other slots
     * @return if merging of at least one item succeed, false otherwise
     */
    public static boolean mergeItemStack(ItemStack itemStack, List<Slot> slots, boolean simulate) {
        if (itemStack.isEmpty())
            return false; //if we are merging empty stack, return

        boolean merged = false;
        //iterate non-empty slots first
        //to try to insert stack into them
        for (Slot slot : slots) {
            if (!slot.isItemValid(itemStack))
                continue; //if itemstack cannot be placed into that slot, continue
            ItemStack stackInSlot = slot.getStack();
            if (!ItemStack.areItemsEqual(itemStack, stackInSlot) ||
                !ItemStack.areItemStackTagsEqual(itemStack, stackInSlot))
                continue; //if itemstacks don't match, continue
            int slotMaxStackSize = Math.min(stackInSlot.getMaxStackSize(), slot.getItemStackLimit(stackInSlot));
            int amountToInsert = Math.min(itemStack.getCount(), slotMaxStackSize - stackInSlot.getCount());
            if (amountToInsert == 0)
                continue; //if we can't insert anything, continue
            //shrink our stack, grow slot's stack and mark slot as changed
            if (!simulate) {
                stackInSlot.grow(amountToInsert);
            }
            itemStack.shrink(amountToInsert);
            slot.onSlotChanged();
            merged = true;
            if (itemStack.isEmpty())
                return true; //if we inserted all items, return
        }

        //then try to insert itemstack into empty slots
        //breaking it into pieces if needed
        for (Slot slot : slots) {
            if (!slot.isItemValid(itemStack))
                continue; //if itemstack cannot be placed into that slot, continue
            if (slot.getHasStack())
                continue; //if slot contains something, continue
            int amountToInsert = Math.min(itemStack.getCount(), slot.getItemStackLimit(itemStack));
            if (amountToInsert == 0)
                continue; //if we can't insert anything, continue
            //split our stack and put result in slot
            ItemStack stackInSlot = itemStack.splitStack(amountToInsert);
            if (!simulate) {
                slot.putStack(stackInSlot);
            }
            merged = true;
            if (itemStack.isEmpty())
                return true; //if we inserted all items, return
        }
        return merged;
    }

    public static int getRedstonePower(World world, BlockPos blockPos, Direction side) {
        BlockPos offsetPos = blockPos.offset(side);
        int worldPower = world.getRedstonePower(offsetPos, side);
        if (worldPower >= 15) {
            return worldPower;
        } else {
            IBlockState offsetState = world.getBlockState(offsetPos);
            if(offsetState.getBlock() instanceof BlockRedstoneWire) {
                int wirePower = offsetState.getValue(BlockRedstoneWire.POWER);
                return Math.max(worldPower, wirePower);
            }
            return worldPower;
        }
    }
}
