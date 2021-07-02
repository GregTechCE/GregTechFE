package gregtech.mixin.accessor;

import net.minecraft.block.Block;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockLootTableGenerator.class)
public interface BlockLootTableGeneratorAccessor {

    @Accessor("SAPLING_DROP_CHANCE")
    static float[] getSaplingDropChance() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static LootTable.Builder oreDrops(Block dropWithSilkTouch, Item drop) {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static LootTable.Builder leavesDrop(Block leaves, Block drop, float... chance) {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static LootTable.Builder dropsNothing() {
        throw new UnsupportedOperationException();
    }
}
