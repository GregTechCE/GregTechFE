package gregtech.mixin;

import gregtech.api.net.PacketMachineRecipesSynchronize;
import gregtech.api.recipes.GTRecipeManager;
import gregtech.mixin.accessor.MinecraftServerAccessor;
import gregtech.mixin.impl.GTServerResourceManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow
    private MinecraftServer server;
    @Shadow
    private List<ServerPlayerEntity> players;

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player) {
        ServerResourceManager resourceManager = ((MinecraftServerAccessor) server).getServerResourceManager();
        GTRecipeManager recipeManager = ((GTServerResourceManager) resourceManager).gregtech_getRecipeManager();

        PacketMachineRecipesSynchronize packet = new PacketMachineRecipesSynchronize(recipeManager);
        packet.sendTo(player);
    }

    @Inject(method = "onDataPacksReloaded", at = @At("TAIL"))
    private void onDataPacksReloaded() {
        ServerResourceManager resourceManager = ((MinecraftServerAccessor) server).getServerResourceManager();
        GTRecipeManager recipeManager = ((GTServerResourceManager) resourceManager).gregtech_getRecipeManager();

        PacketMachineRecipesSynchronize packet = new PacketMachineRecipesSynchronize(recipeManager);
        for (ServerPlayerEntity playerEntity : players) {
            packet.sendTo(playerEntity);
        }
    }
}
