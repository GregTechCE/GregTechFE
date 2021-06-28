package gregtech.mixin;

import gregtech.api.items.util.CustomEnchantableItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//Relevant Fabric issue: FabricMC/fabric#202
@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (stack.getItem() instanceof CustomEnchantableItem item) {
            Enchantment enchantment = (Enchantment) (Object) this;

            if (item.canApplyEnchantment(enchantment)) {
                callbackInfo.setReturnValue(true);
            }
        }
    }
}
