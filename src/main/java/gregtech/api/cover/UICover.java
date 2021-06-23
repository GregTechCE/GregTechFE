package gregtech.api.cover;

import gregtech.api.gui.ModularUI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface UICover {

    default void openUI(ServerPlayerEntity player) {
        CoverBehaviorUIFactory.INSTANCE.openUI((CoverBehavior) this, player);
    }

    ModularUI createUI(PlayerEntity player);

}
