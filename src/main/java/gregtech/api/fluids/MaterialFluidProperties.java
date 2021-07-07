package gregtech.api.fluids;

import com.google.common.base.Preconditions;
import net.minecraft.entity.damage.DamageSource;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public class MaterialFluidProperties {

    public static final String DEFAULT_FLUID_NAME_TEMPLATE = "{material}";
    public static final String DEFAULT_FLUID_TRANSLATION_KEY = "gregtech.fluid.default_fluid";
    public static final int DEFAULT_FLUID_TEMPERATURE = 300;
    public static final int MINIMUM_HOT_FLUID_TEMPERATURE = 400;
    public static final int MOLTEN_FLUID_TEMPERATURE = 1270;
    public static final float DEFAULT_FLUID_BLAST_RESISTANCE = 100.0f;
    public static final int DEFAULT_FLUID_FLOW_SPEED = 4;
    public static final int DEFAULT_FLUID_LUMINOSITY = 0;
    public static final int DEFAULT_FLUID_TICK_RATE = 5;
    public static final int DEFAULT_FLUID_LEVEL_DECREASE_PER_BLOCK = 1;

    private final String fluidNameTemplate;
    private final String translationKey;
    private final boolean isGaseous;
    private final boolean isHotFluid;
    private final int temperature;
    private final float blastResistance;
    private final int flowSpeed;
    private final int netherFlowSpeed;
    private final int luminosity;
    private final int fluidTickRate;
    private final int netherFluidTickRate;
    private final int levelDecreasePerBlock;
    private final int netherLevelDecreasePerBlock;
    private final Pair<DamageSource, Float> damageToEntities;

    public MaterialFluidProperties(Settings settings) {
        this.fluidNameTemplate = settings.fluidNameTemplate;
        this.translationKey = settings.translationKey;
        this.isGaseous = settings.isGaseous;
        this.isHotFluid = settings.isHotFluid;
        this.temperature = settings.temperature;
        this.blastResistance = settings.blastResistance;
        this.flowSpeed = settings.flowSpeed;
        this.netherFlowSpeed = settings.netherFlowSpeed;
        this.luminosity = settings.luminosity;
        this.fluidTickRate = settings.fluidTickRate;
        this.netherFluidTickRate = settings.netherFluidTickRate;
        this.levelDecreasePerBlock = settings.levelDecreasePerBlock;
        this.netherLevelDecreasePerBlock = settings.netherLevelDecreasePerBlock;
        this.damageToEntities = settings.damageToEntities;
    }

    public String createFluidName(String materialName) {
        return this.fluidNameTemplate.replace("{material}", materialName);
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public boolean isGaseous() {
        return isGaseous;
    }

    public boolean isHotFluid() {
        return isHotFluid;
    }

    public int getTemperature() {
        return temperature;
    }

    public float getBlastResistance() {
        return blastResistance;
    }

    public int getFlowSpeed() {
        return flowSpeed;
    }

    public int getNetherFlowSpeed() {
        return netherFlowSpeed;
    }

    public int getLuminosity() {
        return luminosity;
    }

    public int getFluidTickRate() {
        return fluidTickRate;
    }

    public int getNetherFluidTickRate() {
        return netherFluidTickRate;
    }

    public int getLevelDecreasePerBlock() {
        return levelDecreasePerBlock;
    }

    public int getNetherLevelDecreasePerBlock() {
        return netherLevelDecreasePerBlock;
    }

    @Nullable
    public Pair<DamageSource, Float> getDamageToEntities() {
        return damageToEntities;
    }

    public static class Settings {
        private String fluidNameTemplate = DEFAULT_FLUID_NAME_TEMPLATE;
        private String translationKey = DEFAULT_FLUID_TRANSLATION_KEY;
        private boolean isGaseous = false;
        private boolean isHotFluid = false;
        private int temperature = DEFAULT_FLUID_TEMPERATURE;
        private float blastResistance = DEFAULT_FLUID_BLAST_RESISTANCE;
        private int flowSpeed = DEFAULT_FLUID_FLOW_SPEED;
        private int netherFlowSpeed = DEFAULT_FLUID_FLOW_SPEED;
        private int luminosity = DEFAULT_FLUID_LUMINOSITY;
        private int fluidTickRate = DEFAULT_FLUID_TICK_RATE;
        private int netherFluidTickRate = DEFAULT_FLUID_TICK_RATE;
        private int levelDecreasePerBlock = DEFAULT_FLUID_LEVEL_DECREASE_PER_BLOCK;
        private int netherLevelDecreasePerBlock = DEFAULT_FLUID_LEVEL_DECREASE_PER_BLOCK;
        private Pair<DamageSource, Float> damageToEntities;

        public Settings fluidNameTemplate(String fluidNameTemplate) {
            Preconditions.checkArgument(fluidNameTemplate.contains("{material}"), "fluidNameTemplate should contain {material} placeholder");
            this.fluidNameTemplate = fluidNameTemplate;
            return this;
        }

        public Settings translationKey(String translationKey) {
            this.translationKey = translationKey;
            return this;
        }

        public Settings gaseous() {
            this.isGaseous = true;
            return this;
        }

        public Settings temperature(int temperature) {
            Preconditions.checkArgument(temperature > 0, "temperature should be positive");
            this.temperature = temperature;
            this.isHotFluid = temperature >= MINIMUM_HOT_FLUID_TEMPERATURE;
            return this;
        }

        public Settings luminosity(int luminosity) {
            Preconditions.checkArgument(luminosity >= 0 && luminosity <= 15, "luminosity should be in 0..15 range");
            this.luminosity = luminosity;
            return this;
        }

        public Settings blastResistance(float blastResistance) {
            Preconditions.checkArgument(blastResistance > 0, "blastResistance should not be negative");
            this.blastResistance = blastResistance;
            return this;
        }

        public Settings flowSpeed(int flowSpeed) {
            Preconditions.checkArgument(flowSpeed > 0, "flowSpeed must be positive");
            this.flowSpeed = flowSpeed;
            this.netherFlowSpeed = flowSpeed;
            return this;
        }

        public Settings fluidTickRate(int fluidTickRate) {
            Preconditions.checkArgument(fluidTickRate > 0, "fluidTickRate should be positive");
            this.fluidTickRate = fluidTickRate;
            this.netherFluidTickRate = fluidTickRate;
            return this;
        }

        public Settings levelDecreasePerBlock(int levelDecreasePerBlock) {
            Preconditions.checkArgument(levelDecreasePerBlock > 0, "levelDecreasePerBlock should be positive");
            this.levelDecreasePerBlock = levelDecreasePerBlock;
            this.netherLevelDecreasePerBlock = levelDecreasePerBlock;
            return this;
        }

        public Settings netherFlowSpeed(int netherFlowSpeed) {
            Preconditions.checkArgument(netherFlowSpeed > 0, "netherFlowSpeed should be positive");
            this.netherFlowSpeed = netherFlowSpeed;
            return this;
        }

        public Settings netherFluidTickRate(int netherFluidTickRate) {
            Preconditions.checkArgument(netherFluidTickRate > 0, "netherFluidTickRate should be positive");
            this.netherFluidTickRate = netherFluidTickRate;
            return this;
        }

        public Settings netherLevelDecreasePerBlock(int netherLevelDecreasePerBlock) {
            Preconditions.checkArgument(netherLevelDecreasePerBlock > 0, "netherLevelDecreasePerBlock should be positive");
            this.netherLevelDecreasePerBlock = netherLevelDecreasePerBlock;
            return this;
        }

        public Settings damagesEntities(DamageSource damageSource, float amount) {
            Preconditions.checkNotNull(damageSource, "damageSource");
            Preconditions.checkArgument(amount > 0, "amount should be positive");
            this.damageToEntities = Pair.of(damageSource, amount);
            return this;
        }
    }
}
