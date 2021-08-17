package gregtech.api.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.module.api.OrientationKind;
import gregtech.api.util.VoltageTier;

public class EnergyProducerConfig extends EnergyContainerConfig {

    public static final int DEFAULT_MAX_OUTPUT_AMPERAGE = 1;
    public static final int DEFAULT_CAPACITY_MULTIPLIER = 32;

    private final int maxOutputAmperage;
    private final OrientationKind outputDirection;

    protected EnergyProducerConfig(long capacity, VoltageTier tier, int maxOutputAmperage, OrientationKind outputDirection) {
        super(capacity, tier);
        Preconditions.checkNotNull(outputDirection, "outputDirection");
        Preconditions.checkArgument(maxOutputAmperage > 0, "maxOutputAmperage should be positive");
        this.maxOutputAmperage = maxOutputAmperage;
        this.outputDirection = outputDirection;
    }

    public int getMaxOutputAmperage() {
        return maxOutputAmperage;
    }

    public OrientationKind getOutputDirection() {
        return outputDirection;
    }

    public static EnergyProducerConfig of(VoltageTier tier, OrientationKind outputDirection) {
        return of(tier, DEFAULT_CAPACITY_MULTIPLIER, DEFAULT_MAX_OUTPUT_AMPERAGE, outputDirection);
    }

    public static EnergyProducerConfig of(VoltageTier tier, long capacityMultiplier, int maxOutputAmperage, OrientationKind outputDirection) {
        long capacity = capacityMultiplier * tier.getVoltage();
        return new EnergyProducerConfig(capacity, tier, maxOutputAmperage, outputDirection);
    }

    public static class Builder extends EnergyContainerConfig.Builder {

        private int maxOutputAmperage = DEFAULT_MAX_OUTPUT_AMPERAGE;
        private OrientationKind outputDirection;

        public static Builder start() {
            return new Builder();
        }

        @Override
        public Builder tier(VoltageTier tier) {
            return (Builder) super.tier(tier);
        }

        @Override
        public Builder capacity(long capacity) {
            return (Builder) super.capacity(capacity);
        }

        @Override
        public Builder capacityMultiplier(int capacityMultiplier) {
            return (Builder) super.capacityMultiplier(capacityMultiplier);
        }

        public Builder maxOutputAmperage(int maxOutputAmperage) {
            Preconditions.checkArgument(maxOutputAmperage > 0, "maxOutputAmperage must be positive");
            this.maxOutputAmperage = maxOutputAmperage;
            return this;
        }

        public Builder defaultCapacity() {
            return capacityMultiplier(DEFAULT_CAPACITY_MULTIPLIER);
        }

        public Builder outputDirection(OrientationKind outputDirection) {
            Preconditions.checkNotNull(outputDirection, "outputDirection");
            this.outputDirection = outputDirection;
            return this;
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            super.copyPropertiesFrom(builder);
            if (builder instanceof Builder other) {
                this.maxOutputAmperage = other.maxOutputAmperage;
                this.outputDirection = other.outputDirection;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public EnergyProducerConfig build() {
            return new EnergyProducerConfig(this.capacity, this.tier, this.maxOutputAmperage, this.outputDirection);
        }
    }
}
