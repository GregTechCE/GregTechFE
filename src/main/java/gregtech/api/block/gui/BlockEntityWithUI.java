package gregtech.api.block.gui;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.UIFactories;
import gregtech.api.gui.UIHolder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface BlockEntityWithUI extends UIHolder {

    ModularUI createUI(PlayerEntity player);

    default void openUI(ServerPlayerEntity player) {
        UIFactories.BLOCK_ENTITY.openUI(this, player);
    }

    @Override
    default boolean isValid() {
        BlockEntity blockEntity = (BlockEntity) this;
        return !blockEntity.isRemoved();
    }

    @Override
    default boolean isClient() {
        BlockEntity blockEntity = (BlockEntity) this;
        //noinspection ConstantConditions
        return blockEntity.hasWorld() && blockEntity.getWorld().isClient();
    }

    @Override
    default void markDirty() {
        BlockEntity blockEntity = (BlockEntity) this;
        blockEntity.markDirty();
    }
}
