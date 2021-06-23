package gregtech.api.cover;


import com.google.common.collect.Lists;
import gregtech.api.gui.UIHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents cover instance attached on the specific side of meta tile entity
 * Cover filters out interaction and logic of meta tile entity
 * <p>
 * Can implement {@link TickableCover} to listen to meta tile entity updates
 */
@SuppressWarnings("unused")
public abstract class CoverBehavior implements UIHolder {

    private CoverDefinition definition;
    private final Coverable holder;
    private final Direction side;
    private int redstoneSignalOutput;

    public CoverBehavior(Coverable holder, Direction side) {
        this.holder = holder;
        this.side = side;
    }

    final void setCoverDefinition(CoverDefinition definition) {
        this.definition = definition;
    }

    public Coverable getHolder() {
        return holder;
    }

    public Direction getSide() {
        return side;
    }

    public final CoverDefinition getCoverDefinition() {
        return definition;
    }

    public final void setRedstoneSignalOutput(int redstoneSignalOutput) {
        this.redstoneSignalOutput = redstoneSignalOutput;
        this.holder.notifyBlockUpdate();
        this.holder.markDirty();
    }

    public final int getRedstoneSignalOutput() {
        return redstoneSignalOutput;
    }

    public final int getRedstoneSignalInput() {
        return this.holder.getInputRedstoneSignal(side, true);
    }

    public void onRedstoneInputSignalChange(int newSignalStrength) {
    }

    public boolean canConnectRedstone() {
        return false;
    }

    public void writeToNBT(NbtCompound tagCompound) {
        if(redstoneSignalOutput > 0) {
            tagCompound.putInt("RedstoneSignal", redstoneSignalOutput);
        }
    }

    public void readFromNBT(NbtCompound tagCompound) {
        if (tagCompound.contains("RedstoneSignal")) {
            this.redstoneSignalOutput = tagCompound.getInt("RedstoneSignal");
        }
    }

    public void writeInitialSyncData(PacketByteBuf packetBuffer) {
    }

    public void readInitialSyncData(PacketByteBuf packetBuffer) {
    }

    public void readUpdateData(int id, PacketByteBuf packetBuffer) {
    }

    public final void writeUpdateData(int id, Consumer<PacketByteBuf> writer) {
        this.holder.writeCoverData(this, id, writer);
    }

    /**
     * Called on server side to check whether cover can be attached to given meta tile entity
     *
     * @return true if cover can be attached, false otherwise
     */
    public abstract boolean canAttach();

    /**
     * Will be called on server side after the cover attachment to the meta tile entity
     * Cover can change it's internal state here and it will be synced to client with {@link #writeInitialSyncData(PacketByteBuf)}
     *
     * @param itemStack the item cover was attached from
     */
    public void onAttached(ItemStack itemStack) {
    }

    public boolean shouldInteractWithOutputSide() {
        return false;
    }

    public ItemStack getPickItem() {
        return this.definition.getDropItemStack();
    }

    public List<ItemStack> getDrops() {
        return Lists.newArrayList(getPickItem());
    }

    /**
     * Called prior to cover removing on the server side
     * Will also be called during machine dismantling, as machine loses installed covers after that
     */
    public void onRemoved() {
    }
    
    public boolean shouldRenderConnected() {
        return true;
    }

    public boolean canPipePassThrough() {
        return false;
    }

    public boolean canRenderBackside() {
        return true;
    }

    public boolean onLeftClick(PlayerEntity entityPlayer, BlockHitResult hitResult) {
        return false;
    }

    public ActionResult onRightClick(PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
        return ActionResult.PASS;
    }

    public ActionResult onScrewdriverClick(PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
        return ActionResult.PASS;
    }

    /**
     * Called on client side to render this cover on the machine's face
     * It will be automatically translated to prevent Z-fighting with machine faces
     */
    //TODO RENDERING
    /*@Environment(EnvType.CLIENT)
    public abstract void renderCover(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 plateBox, BlockRenderLayer layer);*/

    @Environment(EnvType.CLIENT)
    public boolean canRenderInLayer(RenderLayer renderLayer) {
        return renderLayer == RenderLayer.getCutout();
    }

    //TODO RENDERING
    /*@SideOnly(Side.CLIENT)
    public void renderCoverPlate(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 plateBox, BlockRenderLayer layer) {
        TextureAtlasSprite casingSide = getPlateSprite();
        for (EnumFacing coverPlateSide : EnumFacing.VALUES) {
            boolean isAttachedSide = attachedSide.getAxis() == coverPlateSide.getAxis();
            if (isAttachedSide) {
                Textures.renderFace(renderState, translation, pipeline, coverPlateSide, plateBox, casingSide);
            } else if (coverHolder.getCoverAtSide(coverPlateSide) == null) {
                Textures.renderFace(renderState, translation, pipeline, coverPlateSide, plateBox, casingSide);
            }
        }
    }*/

    //TODO RENDERING
    /*@Environment(EnvType.CLIENT)
    protected Sprite getPlateSprite() {
        return Textures.VOLTAGE_CASINGS[GTValues.LV].getSpriteOnSide(RenderSide.SIDE);
    }*/

    @Override
    public final boolean isValid() {
        return this.holder.isValid() && this.holder.getCoverAtSide(side) == this;
    }

    @Override
    public boolean isClient() {
        return this.holder.getWorld().isClient();
    }

    @Override
    public final void markDirty() {
        this.holder.markDirty();
    }
}
