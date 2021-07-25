package gregtech.mixin;

import com.google.common.base.Preconditions;
import gregtech.api.item.util.CustomEnchantableItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

//Relevant Fabric issue: FabricMC/fabric#202
@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Unique
    private static Enchantment gregtech$currentEnchantment = null;

    @Inject(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void getEnchantment(int power, ItemStack stack, boolean treasureAllowed, List<Enchantment> enchantmentList, Item item, boolean isBook, Iterator<Enchantment> enchantmentIterator, Enchantment enchantment) {
        gregtech$currentEnchantment = enchantment;
    }

    @Redirect(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean isAcceptableItem(EnchantmentTarget target, Item item, int power, ItemStack stack, boolean treasureAllowed) {
        if (item instanceof CustomEnchantableItem enchantableItem) {
            Preconditions.checkNotNull(gregtech$currentEnchantment);
            if (enchantableItem.canApplyEnchantment(gregtech$currentEnchantment)) {
                return true;
            }
        }
        return target.isAcceptableItem(item);
    }
}
