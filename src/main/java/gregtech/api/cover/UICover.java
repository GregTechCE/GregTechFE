package gregtech.api.cover;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.UIFactories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface UICover {

    default void openUI(ServerPlayerEntity player) {
        UIFactories.COVER_BEHAVIOR.openUI((CoverBehavior) this, player);
    }

    ModularUI createUI(PlayerEntity player);

}
