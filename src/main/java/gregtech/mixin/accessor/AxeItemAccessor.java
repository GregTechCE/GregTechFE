package gregtech.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {

    @Invoker
    Optional<BlockState> getStrippedState(BlockState state);
}
