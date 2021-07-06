package gregtech.mixin;

import gregtech.api.recipes.GTRecipeManager;
import gregtech.mixin.impl.GTServerResourceManager;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResourceManager.class)
public abstract class ServerResourceManagerMixin implements GTServerResourceManager {

    @Shadow
    private ReloadableResourceManager resourceManager;
    @Shadow
    private RecipeManager recipeManager;

    @Unique
    private GTRecipeManager gregtech_recipeManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onResourceManagerInit(DynamicRegistryManager registryManager,
                                       CommandManager.RegistrationEnvironment commandEnvironment,
                                       int functionPermissionLevel,
                                       CallbackInfo callbackInfo) {
        this.gregtech_recipeManager = new GTRecipeManager(recipeManager);
        this.resourceManager.registerReloader(this.gregtech_recipeManager);
    }

    @Override
    public GTRecipeManager gregtech_getRecipeManager() {
        return gregtech_recipeManager;
    }
}
