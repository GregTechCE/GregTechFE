package gregtech.mixin;

import gregtech.api.items.util.ItemEntityAwareItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getStack();

    private ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick() {
        ItemStack itemStack = getStack();

        if (itemStack.getItem() instanceof ItemEntityAwareItem item) {
            item.onEntityItemUpdate(itemStack, (ItemEntity) (Object) this);
        }
    }
}
