package gregtech.mixin.accessor;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {

    @Accessor
    ItemStack getTouchDragStack();

    @Accessor
    boolean isTouchIsRightClickDrag();

    @Accessor
    List<Drawable> getDrawables();

    @Accessor
    int getDraggedStackRemainder();

    @Accessor
    ItemStack getTouchDropReturningStack();

    @Accessor
    void setTouchDropReturningStack(ItemStack itemStack);

    @Accessor
    long getTouchDropTime();

    @Accessor
    Slot getTouchDropOriginSlot();

    @Accessor
    int getTouchDropX();

    @Accessor
    int getTouchDropY();


    @Accessor
    void drawSlot(MatrixStack matrices, Slot slot);
}
