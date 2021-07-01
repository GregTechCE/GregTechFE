package gregtech.mixin;

import com.google.common.collect.Multimap;
import gregtech.api.items.util.CustomMaxCountItem;
import gregtech.api.items.util.DynamicAttributeModifierItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract boolean hasTag();

    @Shadow
    public abstract NbtCompound getTag();

    @Shadow
    public abstract Item getItem();

    @Inject(method = "getMaxCount()I", cancellable = true, at = @At("RETURN"))
    private void getMaxCount(CallbackInfoReturnable<Integer> callbackInfo) {
        if (getItem() instanceof CustomMaxCountItem customMaxCountItem) {
            callbackInfo.setReturnValue(customMaxCountItem.getMaxCount((ItemStack) (Object) this));
        }
    }

    @Inject(method = "getAttributeModifiers", cancellable = true, at = @At("RETURN"))
    private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> callbackInfo) {
        if (getItem() instanceof DynamicAttributeModifierItem attributeModifierItem) {
            boolean hasCustomAttributeModifiers = hasTag() && getTag().contains("AttributeModifiers", NbtElement.LIST_TYPE);
            if (!hasCustomAttributeModifiers) {
                Multimap<EntityAttribute, EntityAttributeModifier> result =
                        attributeModifierItem.getAttributeModifiers((ItemStack) (Object) this, slot);
                callbackInfo.setReturnValue(result);
            }
        }
    }
}
