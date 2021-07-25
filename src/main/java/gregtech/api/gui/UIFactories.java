package gregtech.api.gui;

import gregtech.api.GTValues;
import gregtech.api.block.gui.BlockEntityUIFactory;
import gregtech.api.block.gui.BlockEntityWithUI;
import gregtech.api.cover.CoverBehavior;
import gregtech.api.cover.CoverBehaviorUIFactory;
import gregtech.api.item.gui.PlayerInventoryHolder;
import gregtech.api.item.gui.PlayerInventoryUIFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UIFactories {

    public static final UIFactory<CoverBehavior> COVER_BEHAVIOR;
    public static final UIFactory<PlayerInventoryHolder> PLAYER_INVENTORY;
    public static final UIFactory<BlockEntityWithUI> BLOCK_ENTITY;

    private static <T extends UIHolder> UIFactory<T> register(String name, UIFactory<T> factory) {
        return Registry.register(UIFactory.REGISTRY, new Identifier(GTValues.MODID, name), factory);
    }

    public static void ensureInitialized() {
    }

    static {
        COVER_BEHAVIOR = register("cover_behavior", new CoverBehaviorUIFactory());
        PLAYER_INVENTORY = register("player_inventory", new PlayerInventoryUIFactory());
        BLOCK_ENTITY = register("block_entity", new BlockEntityUIFactory());
    }
}
