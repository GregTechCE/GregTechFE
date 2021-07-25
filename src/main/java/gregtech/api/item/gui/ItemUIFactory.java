package gregtech.api.item.gui;

import gregtech.api.gui.ModularUI;
import net.minecraft.entity.player.PlayerEntity;

public interface ItemUIFactory {

    /**
     * Creates new UI basing on given holder. Holder contains information
     * about item stack and hand, and also player
     */
    ModularUI createUI(PlayerInventoryHolder holder, PlayerEntity entityPlayer);

}
