package gregtech.api.block.machine;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MachineBlockEntityType<T extends MachineBlockEntity> extends BlockEntityType<T> {

    private final BlockEntityFactory<T> factory;

    public MachineBlockEntityType(BlockEntityFactory<T> factory) {
        super(null, ImmutableSet.of(), null);
        this.factory = factory;
    }

    @Nullable
    @Override
    public T instantiate(BlockPos pos, BlockState state) {
        return this.factory.create(this, pos, state);
    }

    @Override
    public boolean supports(BlockState state) {
        return state.getBlock() instanceof MachineBlock;
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        T create(BlockEntityType<?> type, BlockPos pos, BlockState state);
    }
}
