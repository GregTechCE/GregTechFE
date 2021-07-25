package gregtech.api.network;

import gregtech.api.gui.UIHolder;
import gregtech.api.gui.impl.ModularUIScreenHandler;
import gregtech.api.recipe.GTRecipeManager;
import gregtech.api.util.GTWorldEventUtil;
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
            GTRecipeManager.handleRecipeSync(client, handler, packet);
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketWorldEvent.CHANNEL,
                (client, handler, buf, responseSender) -> {

            PacketWorldEvent packet = new PacketWorldEvent(buf);
            GTWorldEventUtil.handleWorldEvent(client, packet);
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
