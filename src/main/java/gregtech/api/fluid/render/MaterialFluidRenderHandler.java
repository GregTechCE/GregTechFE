package gregtech.api.fluid.render;

import gregtech.api.GTValues;
import gregtech.api.fluid.MaterialFluid;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.MaterialFlags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public enum MaterialFluidRenderHandler implements FluidRenderHandler, SimpleSynchronousResourceReloadListener, ClientSpriteRegistryCallback {
    INSTANCE;

    private static final Identifier RESOURCE_RELOAD_LISTENER_ID = new Identifier(GTValues.MODID, "fluid_sprite_reload_listener");
    private static final Sprite[] EMPTY_SPRITE_ARRAY = new Sprite[2];

    private final Map<Material, MaterialFluidData> registeredFluids = new HashMap<>();

    public void registerFluid(MaterialFluid fluid) {
        Material material = fluid.getMaterial();

        MaterialFluidData materialFluidData = new MaterialFluidData(fluid.getTexture());
        this.registeredFluids.put(material, materialFluidData);

        FluidRenderHandlerRegistry.INSTANCE.register(fluid.getStill(), this);
        FluidRenderHandlerRegistry.INSTANCE.register(fluid.getFlowing(), this);
    }

    @Override
    public void registerSprites(SpriteAtlasTexture atlasTexture, Registry registry) {
        if (atlasTexture.getId().equals(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)) {
            for (MaterialFluidData fluidData : this.registeredFluids.values()) {
                fluidData.addRequiredSprites(registry);
            }
        }
    }

    @Override
    public void reload(ResourceManager manager) {
        MinecraftClient client = MinecraftClient.getInstance();
        Function<Identifier, Sprite> blockAtlasSpriteGetter = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

        for (MaterialFluidData fluidData : this.registeredFluids.values()) {
            fluidData.reloadSprites(blockAtlasSpriteGetter);
        }
    }

    @Override
    public Sprite[] getFluidSprites(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
        if (state.getFluid() instanceof MaterialFluid materialFluid) {
            Material material = materialFluid.getMaterial();
            MaterialFluidData materialFluidData = this.registeredFluids.get(material);

            if (materialFluidData != null) {
                return materialFluidData.getSprites();
            }
        }
        return EMPTY_SPRITE_ARRAY;
    }

    @Override
    public int getFluidColor(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
        if (state.getFluid() instanceof MaterialFluid materialFluid) {
            Material material = materialFluid.getMaterial();
            MaterialFluidData materialFluidData = this.registeredFluids.get(material);

            if (materialFluidData.shouldTintFluidSprite()) {
                return material.queryPropertyChecked(MaterialFlags.COLOR);
            }
        }
        return 0xFFFFFF;
    }

    @Override
    public Identifier getFabricId() {
        return RESOURCE_RELOAD_LISTENER_ID;
    }

    @Environment(EnvType.CLIENT)
    private static final class MaterialFluidData {

        private final MaterialFluidTexture texture;
        private final Sprite[] sprites;

        public MaterialFluidData(MaterialFluidTexture texture) {
            this.texture = texture;
            this.sprites = new Sprite[2];
        }

        public Sprite[] getSprites() {
            return sprites;
        }

        public boolean shouldTintFluidSprite() {
            return this.texture.shouldTintFluidSprite();
        }

        public void addRequiredSprites(ClientSpriteRegistryCallback.Registry registry) {
            registry.register(this.texture.getStillTexture());
            registry.register(this.texture.getFlowingTexture());
        }

        public void reloadSprites(Function<Identifier, Sprite> blockAtlasSpriteGetter) {
            this.sprites[0] = blockAtlasSpriteGetter.apply(this.texture.getStillTexture());
            this.sprites[1] = blockAtlasSpriteGetter.apply(this.texture.getFlowingTexture());
        }
    }

    static {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(INSTANCE);
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register(INSTANCE);
    }
}
