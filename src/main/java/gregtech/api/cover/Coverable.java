package gregtech.api.cover;

import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.function.Consumer;

public interface Coverable {

    World getWorld();

    BlockPos getPos();

    boolean isFirstTick();

    long getOffsetTimer();

    void markDirty();

    boolean isValid();

    boolean placeCoverOnSide(Direction side, ItemStack itemStack, CoverDefinition definition);

    boolean removeCover(Direction side);

    boolean canPlaceCoverOnSide(Direction side);

    CoverBehavior getCoverAtSide(Direction side);

    void writeCoverData(CoverBehavior behavior, int id, Consumer<PacketByteBuf> writer);

    int getInputRedstoneSignal(Direction side, boolean ignoreCover);

    ItemStack getStackForm();

    double getCoverPlateThickness();

    int getPaintingColor();

    boolean shouldRenderBackSide();

    void notifyBlockUpdate();

    void scheduleRenderUpdate();

    //TODO RENDERING
    /*@Environment(EnvType.CLIENT)
    default void renderCovers(CCRenderState renderState, Matrix4 translation, BlockRenderLayer layer) {
        renderState.lightMatrix.locate(getWorld(), getPos());
        double coverPlateThickness = getCoverPlateThickness();
        IVertexOperation[] platePipeline = new IVertexOperation[] {new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColor()))};
        IVertexOperation[] coverPipeline = new IVertexOperation[] {renderState.lightMatrix};

        for (Direction sideFacing : Direction.values()) {
            CoverBehavior coverBehavior = getCoverAtSide(sideFacing);
            if (coverBehavior == null) continue;
            Cuboid6 plateBox = getCoverPlateBox(sideFacing, coverPlateThickness);

            if (coverBehavior.canRenderInLayer(layer) && coverPlateThickness > 0) {
                renderState.preRenderWorld(getWorld(), getPos());
                coverBehavior.renderCoverPlate(renderState, translation, platePipeline, plateBox, layer);
            }
            if (coverBehavior.canRenderInLayer(layer)) {
                coverBehavior.renderCover(renderState, translation.copy(), coverPipeline, plateBox, layer);
                if (coverPlateThickness == 0.0 && shouldRenderBackSide() && coverBehavior.canRenderBackside()) {
                    //machine is full block, but still not opaque - render cover on the back side too
                    Matrix4 backTranslation = translation.copy();
                    if (sideFacing.getAxis().isVertical()) {
                        REVERSE_VERTICAL_ROTATION.apply(backTranslation);
                    } else {
                        REVERSE_HORIZONTAL_ROTATION.apply(backTranslation);
                    }
                    backTranslation.translate(-sideFacing.getXOffset(), -sideFacing.getYOffset(), -sideFacing.getZOffset());
                    coverBehavior.renderCover(renderState, backTranslation, coverPipeline, plateBox, layer);
                }
            }
        }
    }*/

    default VoxelShape getCoverCollisionShape() {
        ArrayList<VoxelShape> coverCollisions = new ArrayList<>();
        double plateThickness = getCoverPlateThickness();

        if (plateThickness > 0.0) {
            for (Direction side : Direction.values()) {
                if (getCoverAtSide(side) != null) {
                    VoxelShape coverBox = getCoverPlateBox(side, plateThickness);
                    coverCollisions.add(coverBox);
                }
            }
        }
        return coverCollisions.stream().reduce(VoxelShapes.empty(), VoxelShapes::union);
    }

    static boolean doesCoverCollide(Direction side, VoxelShape collisionBox, double plateThickness) {
        if (side == null) {
            return false;
        }
        if (plateThickness > 0.0) {
            VoxelShape coverPlateBox = getCoverPlateBox(side, plateThickness);
            return VoxelShapes.matchesAnywhere(coverPlateBox, collisionBox, BooleanBiFunction.AND);
        }
        return false;
    }

    static Direction determineGridSide(BlockHitResult result) {
        return GTUtility.determineWrenchingSide(result.getSide(),
            (float) (result.getPos().x - result.getBlockPos().getX()),
            (float) (result.getPos().y - result.getBlockPos().getY()),
            (float) (result.getPos().z - result.getBlockPos().getZ()));
    }

    static VoxelShape getCoverPlateBox(Direction side, double plateThickness) {
        return switch (side) {
            case UP -> VoxelShapes.cuboid(0.0, 1.0 - plateThickness, 0.0, 1.0, 1.0, 1.0);
            case DOWN -> VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, plateThickness, 1.0);
            case NORTH -> VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, plateThickness);
            case SOUTH -> VoxelShapes.cuboid(0.0, 0.0, 1.0 - plateThickness, 1.0, 1.0, 1.0);
            case WEST -> VoxelShapes.cuboid(0.0, 0.0, 0.0, plateThickness, 1.0, 1.0);
            case EAST -> VoxelShapes.cuboid(1.0 - plateThickness, 0.0, 0.0, 1.0, 1.0, 1.0);
        };
    }
}
