package gregtech.api.block.machine;

import alexiil.mc.lib.attributes.Simulation;
import gregtech.api.block.GTBlockEntityType;
import gregtech.api.block.GTBlockTags;
import gregtech.api.block.machine.module.MachineModuleCollection;
import gregtech.api.block.machine.module.MachineModuleContainer;
import gregtech.api.block.util.AutoTaggedBlock;
import gregtech.api.block.util.LootTableAwareBlock;
import gregtech.api.block.util.ModelStateProviderBlock;
import gregtech.api.capability.block.WrenchableBlock;
import gregtech.api.render.model.state.ModelState;
import gregtech.api.render.model.state.ModelStateManager;
import gregtech.mixin.accessor.BlockLootTableGeneratorAccessor;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class MachineBlock extends Block implements ModelStateProviderBlock, BlockEntityProvider, AutoTaggedBlock, LootTableAwareBlock, WrenchableBlock {

    protected final MachineModuleCollection moduleCollection;
    protected final ModelStateManager<Block> modelStateManager;

    public MachineBlock(Settings settings) {
        super(settings);

        MachineModuleCollection.Builder moduleBuilder = new MachineModuleCollection.Builder();
        appendMachineModules(moduleBuilder);
        this.moduleCollection = moduleBuilder.build();

        ModelStateManager.Builder<Block> stateBuilder = new ModelStateManager.Builder<>(this);
        appendModelStateProperties(stateBuilder);
        this.modelStateManager = stateBuilder.build();
    }

    protected abstract void appendMachineModules(MachineModuleCollection.Builder builder);

    protected void appendModelStateProperties(ModelStateManager.Builder<Block> builder) {
        this.moduleCollection.appendModelStateProperties(builder);
    }

    @Override
    public void addBlockTags(Set<Tag.Identified<Block>> outTags) {
        outTags.add(BlockTags.PICKAXE_MINEABLE);
        outTags.add(GTBlockTags.WRENCH_MINEABLE);
    }

    @Override
    public LootTable.Builder generateLootTable() {
        return LootTable.builder().pool(BlockLootTableGeneratorAccessor.addSurvivesExplosionCondition(this,
                LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(ItemEntry.builder(this))
        ));
    }

    @Nullable
    public static MachineBlockEntity getMachineAt(BlockView world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MachineBlockEntity machineBlockEntity) {
            return machineBlockEntity;
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        MachineBlockEntity blockEntity = getMachineAt(world, pos);

        if (blockEntity != null && placer != null) {
            Direction machineOrientation = placer.getHorizontalFacing();
            blockEntity.attemptSetOrientation(machineOrientation, placer, Simulation.ACTION);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            MachineBlockEntity blockEntity = getMachineAt(world, pos);

            if (blockEntity != null) {
                DefaultedList<ItemStack> droppedItems = DefaultedList.of();
                blockEntity.clearInventory(droppedItems, Simulation.ACTION);

                ItemScatterer.spawn(world, pos, droppedItems);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean attemptWrench(BlockView world, BlockPos pos, BlockState state, @Nullable PlayerEntity player, Direction wrenchSide, Simulation simulation) {
        MachineBlockEntity blockEntity = getMachineAt(world, pos);

        if (blockEntity != null) {
            return blockEntity.attemptSetOrientation(wrenchSide, player, simulation);
        }
        return false;
    }

    public MachineModuleContainer createModuleContainer(MachineBlockEntity machine) {
        return this.moduleCollection.createModuleContainer(machine);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return GTBlockEntityType.MACHINE.instantiate(pos, state);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected static <T extends BlockEntity, E extends MachineBlockEntity> BlockEntityTicker<T> checkType(BlockEntityType<T> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<T>) (Object) ticker : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        MachineTickType tickType = MachineTickType.of(world);
        if (this.moduleCollection.needsTicking(tickType)) {
            return checkType(type, GTBlockEntityType.MACHINE, MachineBlockEntity::tick);
        }
        return null;
    }

    @Override
    public ModelStateManager<Block> getModelStateManager() {
        return this.modelStateManager;
    }

    @Override
    public ModelState<Block> getModelState(BlockRenderView blockView, BlockState state, BlockPos pos) {
        if (blockView instanceof RenderAttachedBlockView renderAttachedBlockView) {
            Object attachment = renderAttachedBlockView.getBlockEntityRenderAttachment(pos);

            if (attachment instanceof MachineBlockEntityAttachment machineBlockEntityAttachment) {
                return machineBlockEntityAttachment.getModelState();
            }
        }
        return this.modelStateManager.getDefaultState();
    }

    @Override
    public ModelState<Block> getModelStateForItem(ItemStack itemStack) {
        return this.modelStateManager.getDefaultState();
    }
}
