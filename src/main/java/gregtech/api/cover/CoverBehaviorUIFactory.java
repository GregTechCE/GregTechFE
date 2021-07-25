package gregtech.api.cover;

import gregtech.api.capability.internal.GTInternalAttributes;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.UIFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CoverBehaviorUIFactory extends UIFactory<CoverBehavior> {

    @Override
    protected ModularUI createUITemplate(CoverBehavior holder, PlayerEntity entityPlayer) {
        return ((UICover) holder).createUI(entityPlayer);
    }

    @Override
    public CoverBehavior readHolderFromSyncData(World world, PacketByteBuf syncData) {
        BlockPos blockPos = syncData.readBlockPos();
        Direction attachedSide = syncData.readEnumConstant(Direction.class);

        Coverable coverable = GTInternalAttributes.COVERABLE.getFirstOrNull(world, blockPos);
        if (coverable == null) {
            throw new IllegalStateException("Couldn't find coverable implementation at " + blockPos);
        }

        CoverBehavior coverBehavior = coverable.getCoverAtSide(attachedSide);
        if (coverBehavior == null) {
            throw new IllegalStateException("Couldn't find attached cover behavior at side " + attachedSide + " of " + blockPos);
        }
        return coverBehavior;
    }

    @Override
    public void writeHolderToSyncData(PacketByteBuf syncData, CoverBehavior holder) {
        Coverable coverHolder = holder.getHolder();

        syncData.writeBlockPos(coverHolder.getPos());
        syncData.writeEnumConstant(holder.getSide());
    }
}
