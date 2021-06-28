package gregtech.api.gui;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import gregtech.api.cover.CoverBehavior;
import gregtech.api.cover.CoverBehaviorUIFactory;
import gregtech.api.items.gui.PlayerInventoryHolder;
import gregtech.api.items.gui.PlayerInventoryUIFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UIFactories {

    public static final UIFactory<CoverBehavior> COVER_BEHAVIOR;
    public static final UIFactory<PlayerInventoryHolder> PLAYER_INVENTORY;

    private static <T extends UIHolder> UIFactory<T> register(String name, UIFactory<T> factory) {
        return Registry.register(UIFactory.REGISTRY, new Identifier(GTValues.MODID, name), factory);
    }

    public static void init() {
        Preconditions.checkNotNull(PLAYER_INVENTORY);
    }

    static {
        COVER_BEHAVIOR = register("cover_behavior", new CoverBehaviorUIFactory());
        PLAYER_INVENTORY = register("player_inventory", new PlayerInventoryUIFactory());
    }
}
