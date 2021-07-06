package gregtech.mixin;

import com.mojang.authlib.GameProfile;
import gregtech.api.recipes.GTRecipeManager;
import gregtech.mixin.impl.GTClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.recipe.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements GTClientPlayNetworkHandler {

    @Shadow
    private RecipeManager recipeManager;

    @Unique
    private GTRecipeManager gregtech_recipeManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onHandlerInit(MinecraftClient client,
                               Screen screen,
                               ClientConnection connection,
                               GameProfile profile,
                               CallbackInfo callbackInfo) {
        this.gregtech_recipeManager = new GTRecipeManager(recipeManager);
    }

    @Override
    public GTRecipeManager gregtech_getRecipeManager() {
        return gregtech_recipeManager;
    }
}
