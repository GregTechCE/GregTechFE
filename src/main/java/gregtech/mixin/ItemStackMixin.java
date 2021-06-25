package gregtech.mixin;

import gregtech.api.items.util.CustomMaxCountItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Item getItem();

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getMaxCount()I", cancellable = true, at = @At("RETURN"))
    private void getMaxCount(CallbackInfoReturnable<Integer> callbackInfo) {
        Item item = getItem();
        if (item instanceof CustomMaxCountItem customMaxCountItem) {
            callbackInfo.setReturnValue(customMaxCountItem.getMaxCount((ItemStack) (Object) this));
        }
    }
}
