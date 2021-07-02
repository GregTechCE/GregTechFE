package gregtech.api.block.util;

import net.minecraft.loot.LootTable;

public interface LootTableAwareBlock {

    LootTable.Builder generateLootTable();
}
