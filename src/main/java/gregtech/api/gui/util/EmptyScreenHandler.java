package gregtech.api.gui.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.ScreenHandlerSyncHandler;

public class EmptyScreenHandler extends ScreenHandler {

    public EmptyScreenHandler() {
        super(null, 0);
    }

    @Override
    public void addListener(ScreenHandlerListener listener) {
    }

    @Override
    public void updateSyncHandler(ScreenHandlerSyncHandler handler) {
    }

    @Override
    public void removeListener(ScreenHandlerListener listener) {
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
