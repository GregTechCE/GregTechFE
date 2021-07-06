package gregtech.api.fluids;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.collect.ImmutableMap;
import gregtech.api.unification.Materials;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.material.properties.FluidProperties;
import net.minecraft.util.Identifier;

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

            //noinspection ConstantConditions
            Optional<FluidProperties> fluidProperties = material.queryProperty(MaterialFlags.FLUID_PROPERTIES);
            if (fluidProperties.isEmpty()) {
                continue;
            }

            for (MaterialFluidKind fluidKind : MaterialFluidKind.values()) {
                MaterialFluidId fluidId = new MaterialFluidId(fluidKind, material);

                FluidKey builtinFluidKey = BUILTIN_FLUIDS.get(fluidId);
                if (builtinFluidKey != null) {
                    this.registeredMaterialFluids.put(fluidId, builtinFluidKey);
                    continue;
                }

                if (fluidKind.shouldGenerateFor(fluidProperties.get())) {

                }
            }
        }
    }
}
