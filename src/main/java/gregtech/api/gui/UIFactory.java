package gregtech.api.gui;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import gregtech.api.gui.impl.ModularUIScreen;
import gregtech.api.gui.impl.ModularUIScreenHandler;
import gregtech.api.net.NetworkPacket;
import gregtech.api.net.PacketUIOpen;
import gregtech.api.net.PacketUIWidgetUpdate;
import gregtech.api.util.GTUtility;
import gregtech.mixin.ServerPlayerEntityMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Optional;

/**
 * Implement and register to {@link #REGISTRY} to be able to create and open ModularUI's
 * createUITemplate should return equal gui both on server and client side, or sync will break!
 *
 * @param <E> UI holder type
 */
public abstract class UIFactory<E extends UIHolder> {

    @SuppressWarnings("unchecked")
    public static Registry<UIFactory<?>> REGISTRY =
            FabricRegistryBuilder.createSimple((Class<UIFactory<?>>) (Object) UIFactory.class,
                    new Identifier(GTValues.MODID, "ui_factory"))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static <T extends UIFactory<?>> T register(Identifier id, T factory) {
        return Registry.register(REGISTRY, id, factory);
    }

    public final void openUI(E holder, ServerPlayerEntity player) {
        if (GTUtility.isFakePlayer(player)) {
            return;
        }
        ModularUI uiTemplate = createUITemplate(holder, player);
        uiTemplate.initWidgets();

        //Code below based on the code in ServerPlayerEntity#openHandledScreen
        if (player.currentScreenHandler != player.playerScreenHandler) {
            player.closeHandledScreen();
        }

        ((ServerPlayerEntityMixin) player).incrementScreenHandlerSyncId();

        int screenHandlerSyncId = ((ServerPlayerEntityMixin) player).getScreenHandlerSyncId();
        ModularUIScreenHandler screenHandler = new ModularUIScreenHandler(uiTemplate, screenHandlerSyncId);

        //accumulate all initial updates of widgets in open packet
        List<PacketUIWidgetUpdate> widgetUpdates = screenHandler.accumulateWidgetUpdates();
        NetworkPacket packet = new PacketUIOpen<>(this, holder, screenHandler.syncId, widgetUpdates);
        packet.sendTo(player);

        ((ServerPlayerEntityMixin) player).onSpawn(screenHandler);
        player.currentScreenHandler = screenHandler;
    }

    @Environment(EnvType.CLIENT)
    public final void initClientUI(MinecraftClient client, PacketUIOpen<E> packet) {
        client.submit(() -> {
            ClientPlayerEntity entityPlayer = client.player;
            Preconditions.checkNotNull(entityPlayer);

            ModularUI uiTemplate = createUITemplate(packet.uiHolder, entityPlayer);
            uiTemplate.initWidgets();

            ModularUIScreen modularUIScreen = new ModularUIScreen(packet.windowId, entityPlayer.getInventory(), uiTemplate);
            ModularUIScreenHandler screenHandler = modularUIScreen.getScreenHandler();

            for (PacketUIWidgetUpdate widgetUpdate : packet.initialWidgetUpdates) {
                screenHandler.handleWidgetUpdate(widgetUpdate);
            }

            entityPlayer.currentScreenHandler = screenHandler;
            client.openScreen(modularUIScreen);
        });
    }

    protected abstract ModularUI createUITemplate(E holder, PlayerEntity entityPlayer);

    public abstract E readHolderFromSyncData(PacketByteBuf syncData);

    public abstract void writeHolderToSyncData(PacketByteBuf syncData, E holder);

}
