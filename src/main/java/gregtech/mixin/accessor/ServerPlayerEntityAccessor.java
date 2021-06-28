package gregtech.mixin.accessor;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor {

    @Invoker
    void incrementScreenHandlerSyncId();

    @Accessor
    int getScreenHandlerSyncId();

    @Invoker
    void onSpawn(ScreenHandler screenHandler);
}
