package gregtech.api.net;

import gregtech.mixin.impl.GTClientPlayNetworkHandler;
import gregtech.api.gui.UIHolder;
import gregtech.api.gui.impl.ModularUIScreenHandler;
import gregtech.api.recipes.GTRecipeManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkHandler {

    @Environment(EnvType.CLIENT)
    public static void registerClientBoundPackets() {
        ClientPlayNetworking.registerGlobalReceiver(PacketUIOpen.CHANNEL,
                (client, handler, buf, responseSender) -> {

            PacketUIOpen<UIHolder> packet = new PacketUIOpen<>(buf);
            packet.uiFactory.initClientUI(client, packet);
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketUIWidgetUpdate.CHANNEL,
                (client, handler, buf, responseSender) -> {

            PacketUIWidgetUpdate packet = new PacketUIWidgetUpdate(buf);
            ModularUIScreenHandler.handleWidgetUpdate(client, packet);
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketMachineRecipesSynchronize.CHANNEL,
                (client, handler, buf, responseSender) -> {

            PacketMachineRecipesSynchronize packet = new PacketMachineRecipesSynchronize(buf);
            GTRecipeManager recipeManager = ((GTClientPlayNetworkHandler) handler).gregtech_getRecipeManager();
            recipeManager.setRecipes(packet.getRecipes());
        });
    }

    public static void registerServerBoundPackets() {
        ServerPlayNetworking.registerGlobalReceiver(PacketUIClientAction.CHANNEL,
                (server, player, handler, buf, responseSender) -> {

            PacketUIClientAction packet = new PacketUIClientAction(buf);
            ModularUIScreenHandler.handleClientAction(server, player, packet);
        });
    }
}
