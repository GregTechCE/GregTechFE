package gregtech.api.unification.material.properties;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import gregtech.api.unification.material.flags.MaterialFlags;
import gregtech.api.unification.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class OreProperties {

    private final int oreMultiplier;
    private final int byproductMultiplier;
    private final Map<OreProcessingStep, Material> oreByproducts;

    private final boolean disableDirectSmelting;
    private final Material washedIn;
    private final Material separatedOnto;
    private final Material smeltedInto;

    public OreProperties(Settings settings) {
        this.oreMultiplier = settings.oreMultiplier;
        this.byproductMultiplier = settings.byproductMultiplier;
        this.oreByproducts = ImmutableMap.copyOf(settings.oreByproducts);

        this.disableDirectSmelting = settings.disableDirectSmelting;
        this.washedIn = settings.washedIn;
        this.separatedOnto = settings.separatedOnto;
        this.smeltedInto = settings.smeltedInto;
    }

    @Nullable
    public Material getByproductMaterial(OreProcessingStep processingStep) {
        while (processingStep != null && !oreByproducts.containsKey(processingStep)) {
            processingStep = processingStep.getFallbackStep();
        }

        if (processingStep == null) {
            return null;
        }
        return this.oreByproducts.get(processingStep);
    }

    public int getOreMultiplier() {
        return oreMultiplier;
    }

    public int getByproductMultiplier() {
        return byproductMultiplier;
    }

    public boolean isDisableDirectSmelting() {
        return disableDirectSmelting;
    }

    @Nullable
    public Material getWashedIn() {
        return washedIn;
    }

    @Nullable
    public Material getSeparatedOnto() {
        return separatedOnto;
    }

    @Nullable
    public Material getSmeltedInto() {
        return smeltedInto;
    }

    public static class Settings {
        private final Map<OreProcessingStep, Material> oreByproducts = new HashMap<>();
        private int oreMultiplier = 1;
        private int byproductMultiplier = 1;

        private boolean disableDirectSmelting;
        private Material washedIn;
        private Material separatedOnto;
        private Material smeltedInto;

        private boolean isByproductMaterialValid(OreProcessingStep processingStep, Material material) {
            if (material.hasFlag(MaterialFlags.GENERATE_DUST)) {
                return true;
            }
            if (processingStep.isAllowFluid()) {
                return material.hasFlag(MaterialFlags.FLUID_PROPERTIES);
            }
            return false;
        }

        public Settings byproduct(OreProcessingStep processingStep, Material byproduct) {
            Preconditions.checkNotNull(processingStep, "processingStep");
            Preconditions.checkNotNull(byproduct, "byproduct");
            Preconditions.checkArgument(isByproductMaterialValid(processingStep, byproduct), "Invalid byproduct material");

            this.oreByproducts.put(processingStep, byproduct);
            return this;
        }

        public Settings oreMultiplier(int oreMultiplier) {
            Preconditions.checkArgument(oreMultiplier > 0, "oreMultiplier > 0");
            this.oreMultiplier = oreMultiplier;
            return this;
        }

        public Settings byproductMultiplier(int byproductMultiplier) {
            Preconditions.checkArgument(byproductMultiplier > 0, "byproductMultiplier > 0");
            this.byproductMultiplier = byproductMultiplier;
            return this;
        }

        public Settings cannotBeDirectlySmelted() {
            this.disableDirectSmelting = true;
            return this;
        }


        public Settings washedIn(Material washedIn) {
            Preconditions.checkNotNull(washedIn, "washedIn");
            this.washedIn = washedIn;
            return this;
        }

        public Settings separatedOnto(Material separatedOnto) {
            Preconditions.checkNotNull(separatedOnto, "separatedOnto");
            this.separatedOnto = separatedOnto;
            return this;
        }

        public Settings smeltedInto(Material smeltedInto) {
            Preconditions.checkNotNull(smeltedInto, "smeltedInto");
            this.smeltedInto = smeltedInto;
            return this;
        }
    }

}
