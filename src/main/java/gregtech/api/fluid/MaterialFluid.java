package gregtech.api.fluid;

import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeyCustomiser;
import alexiil.mc.lib.attributes.fluid.volume.FluidTemperature;
import gregtech.api.fluid.properties.MaterialFluidProperties;
import gregtech.api.fluid.render.MaterialFluidTexture;
import gregtech.api.fluid.util.MaterialFluidHolder;
import gregtech.api.item.material.MaterialItemForms;
import gregtech.api.item.material.MaterialItemRegistry;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.material.properties.FluidProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class MaterialFluid extends FlowableFluid implements FluidKeyCustomiser {

    protected final Material material;
    protected final MaterialFluidHolder fluidHolder;
    protected final MaterialFluidProperties properties;
    protected final MaterialFluidTexture texture;
    protected final Text fluidName;

    public MaterialFluid(Material material, MaterialFluidHolder fluidHolder, FluidProperties properties) {
        this.material = material;
        this.fluidHolder = fluidHolder;
        this.properties = properties.getProperties();
        this.texture = properties.getTexture();
        this.fluidName = new TranslatableText(this.properties.getTranslationKey(), this.material.getDisplayName());
    }

    public Material getMaterial() {
        return material;
    }

    public MaterialFluidTexture getTexture() {
        return texture;
    }

    public Text getFluidName() {
        return fluidName;
    }

    @Override
    public void customiseKey(FluidKey.FluidKeyBuilder fluidKeyBuilder) {
        fluidKeyBuilder.setName(this.fluidName);

        if (this.properties.isGaseous()) {
            fluidKeyBuilder.setGas();
        }

        if (this.texture.shouldTintFluidSprite()) {
            int materialColor = this.material.queryPropertyChecked(MaterialFlags.COLOR);
            fluidKeyBuilder.setRenderColor(materialColor);
        }

        int temperature = this.properties.getTemperature();
        fluidKeyBuilder.setTemperature(new SimpleFluidTemperature(temperature));
        fluidKeyBuilder.setLuminosity(this.properties.getLuminosity());
    }

    @Override
    public Fluid getFlowing() {
        return this.fluidHolder.getFlowing();
    }

    @Override
    public Fluid getStill() {
        return this.fluidHolder.getStill();
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return this.fluidHolder.getBlock().getDefaultState()
                .with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    @Override
    public Item getBucketItem() {
        return MaterialItemRegistry.INSTANCE.getMaterialItem(MaterialItemForms.BUCKET, material);
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        if (this.properties.isHotFluid()) {
            world.syncWorldEvent(WorldEvents.LAVA_EXTINGUISHED, pos, 0);
        } else {
            BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            Block.dropStacks(state, world, pos, blockEntity);
        }
    }

    @Override
    protected int getFlowSpeed(WorldView world) {
        return world.getDimension().isUltrawarm() ? this.properties.getNetherFlowSpeed() : this.properties.getFlowSpeed();
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return world.getDimension().isUltrawarm() ? this.properties.getNetherLevelDecreasePerBlock() : this.properties.getLevelDecreasePerBlock();
    }

    @Override
    public int getTickRate(WorldView world) {
        return world.getDimension().isUltrawarm() ? this.properties.getNetherFluidTickRate() : this.properties.getFluidTickRate();
    }

    @Override
    protected float getBlastResistance() {
        return this.properties.getBlastResistance();
    }

    @Nullable
    @Override
    protected ParticleEffect getParticle() {
        return this.properties.isHotFluid() ? ParticleTypes.DRIPPING_LAVA : ParticleTypes.DRIPPING_WATER;
    }

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return this.properties.isHotFluid() ?
                Optional.of(SoundEvents.ITEM_BUCKET_FILL_LAVA) :
                Optional.of(SoundEvents.ITEM_BUCKET_FILL);
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getFlowing() || fluid == getStill();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected boolean isInfinite() {
        return false;
    }

    public static class Flowing extends MaterialFluid {

        public Flowing(Material material, MaterialFluidHolder fluidHolder, FluidProperties properties) {
            super(material, fluidHolder, properties);
            this.fluidHolder.setFlowing(this);
        }

        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        public boolean isStill(FluidState state) {
            return false;
        }
    }

    public static class Still extends MaterialFluid {

        public Still(Material material, MaterialFluidHolder fluidHolder, FluidProperties properties) {
            super(material, fluidHolder, properties);
            this.fluidHolder.setStill(this);
        }

        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isStill(FluidState state) {
            return true;
        }
    }

    protected static class SimpleFluidTemperature implements FluidTemperature.DiscreteFluidTemperature {

        private final int temperature;

        public SimpleFluidTemperature(int temperature) {
            this.temperature = temperature;
        }

        @Override
        public double getTemperature(FluidKey fluidKey) {
            return temperature;
        }
    }
}
