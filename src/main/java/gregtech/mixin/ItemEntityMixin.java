package gregtech.mixin;

import gregtech.api.item.util.ItemEntityAwareItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getStack();

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "tick()V", at = @At("HEAD"))
    private void tick() {
        ItemStack itemStack = getStack();

        if (itemStack.getItem() instanceof ItemEntityAwareItem item) {
            item.onEntityItemUpdate(itemStack, (ItemEntity) (Object) this);
        }
    }
}
