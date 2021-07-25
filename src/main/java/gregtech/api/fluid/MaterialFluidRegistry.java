package gregtech.api.fluid;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.collect.ImmutableMap;
import gregtech.api.fluid.util.MaterialFluidHolder;
import gregtech.api.fluid.util.MaterialFluidId;
import gregtech.api.fluid.util.MaterialFluidKind;
import gregtech.api.fluid.render.MaterialFluidRenderHandler;
import gregtech.api.unification.Materials;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.FluidProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MaterialFluidRegistry {

    private static final Map<MaterialFluidId, FluidKey> BUILTIN_FLUIDS = ImmutableMap.of(
            new MaterialFluidId(MaterialFluidKind.FLUID, Materials.Water), FluidKeys.WATER,
            new MaterialFluidId(MaterialFluidKind.FLUID, Materials.Lava), FluidKeys.LAVA
    );

    public static final MaterialFluidRegistry INSTANCE = new MaterialFluidRegistry();

    private final Map<MaterialFluidId, FluidKey> registeredMaterialFluids = new HashMap<>();

    private MaterialFluidRegistry() {
    }

    public FluidKey getMaterialFluid(MaterialFluidId fluidId) {
        return registeredMaterialFluids.getOrDefault(fluidId, FluidKeys.EMPTY);
    }

    public FluidKey getMaterialFluid(Material material, MaterialFluidKind fluidKind) {
        return getMaterialFluid(new MaterialFluidId(fluidKind, material));
    }

    public FluidVolume getMaterialFluid(Material material, FluidAmount amount, MaterialFluidKind fluidKind) {
        return getMaterialFluid(material, fluidKind).withAmount(amount);
    }

    public FluidKey getMaterialFluid(Material material) {
        return getMaterialFluid(material, MaterialFluidKind.FLUID);
    }

    public FluidVolume getMaterialFluid(Material material, FluidAmount amount) {
        return getMaterialFluid(material, amount, MaterialFluidKind.FLUID).withAmount(amount);
    }

    public void registerMaterialFluids() {
        for (Identifier materialId : Material.REGISTRY.getIds()) {
            Material material = Material.REGISTRY.get(materialId);

            for (MaterialFluidKind fluidKind : MaterialFluidKind.values()) {
                MaterialFluidId fluidId = new MaterialFluidId(fluidKind, material);

                FluidKey builtinFluidKey = BUILTIN_FLUIDS.get(fluidId);
                if (builtinFluidKey != null) {
                    this.registeredMaterialFluids.put(fluidId, builtinFluidKey);
                    continue;
                }

                //noinspection ConstantConditions
                Optional<FluidProperties> fluidProperties = fluidKind.queryFluidProperties(material);
                if (fluidProperties.isEmpty()) {
                    continue;
                }

                if (fluidKind.shouldGenerateFor(material)) {
                    MaterialFluidHolder fluidHolder = createAndRegisterMaterialFluid(fluidId, fluidProperties.get());
                    FluidKey fluidKey = FluidKeys.get(fluidHolder.getStill());
                    this.registeredMaterialFluids.put(fluidId, fluidKey);
                }
            }
        }
    }

    private MaterialFluidHolder createAndRegisterMaterialFluid(MaterialFluidId fluidId, FluidProperties fluidProperties) {
        Material material = fluidId.getMaterial();
        MaterialFluidHolder fluidHolder = new MaterialFluidHolder();

        Identifier materialId = material.getName();
        String fluidName = fluidProperties.getProperties().createFluidName(materialId.getPath());
        Identifier fluidRegistryId = new Identifier(materialId.getNamespace(), fluidName);
        Identifier flowingFluidRegistryId = new Identifier(materialId.getNamespace(), "flowing_" + fluidName);

        MaterialFluid stillFluid = new MaterialFluid.Still(material, fluidHolder, fluidProperties);
        MaterialFluid flowingFluid = new MaterialFluid.Flowing(material, fluidHolder, fluidProperties);

        Registry.register(Registry.FLUID, fluidRegistryId, stillFluid);
        Registry.register(Registry.FLUID, flowingFluidRegistryId, flowingFluid);

        FluidBlock fluidBlock = new MaterialFluidBlock(material, fluidHolder, fluidProperties.getProperties());
        Registry.register(Registry.BLOCK, fluidRegistryId, fluidBlock);

        return fluidHolder;
    }

    @Environment(EnvType.CLIENT)
    private void registerMaterialFluidClient(MaterialFluid fluid) {
        MaterialFluidRenderHandler.INSTANCE.registerFluid(fluid);
    }

    @Environment(EnvType.CLIENT)
    public void registerMaterialFluidsClient() {
        for (FluidKey fluidKey : this.registeredMaterialFluids.values()) {
            if (fluidKey.getRawFluid() instanceof MaterialFluid fluid) {
                registerMaterialFluidClient(fluid);
            }
        }
    }
}
