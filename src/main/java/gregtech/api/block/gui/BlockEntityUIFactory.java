package gregtech.api.block.gui;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.UIFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEntityUIFactory extends UIFactory<BlockEntityWithUI> {

    @Override
    protected ModularUI createUITemplate(BlockEntityWithUI holder, PlayerEntity entityPlayer) {
        return holder.createUI(entityPlayer);
    }

    @Override
    public BlockEntityWithUI readHolderFromSyncData(World world, PacketByteBuf syncData) {
        BlockPos blockPos = syncData.readBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (blockEntity instanceof BlockEntityWithUI blockEntityWithUI) {
            return blockEntityWithUI;
        }
        return null;
    }

    @Override
    public void writeHolderToSyncData(PacketByteBuf syncData, BlockEntityWithUI holder) {
        BlockEntity blockEntity = (BlockEntity) holder;
        syncData.writeBlockPos(blockEntity.getPos());
    }
}
