package gregtech.api.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.util.VoltageTier;

public class EnergyConsumerConfig extends EnergyContainerConfig {

    public static final int DEFAULT_MAX_INPUT_AMPERAGE = 2;
    public static final int DEFAULT_CAPACITY_MULTIPLIER = 128;

    private final int maxInputAmperage;

    protected EnergyConsumerConfig(long capacity, VoltageTier tier, int maxInputAmperage) {
        super(capacity, tier);
        Preconditions.checkArgument(maxInputAmperage > 0, "maxInputAmperage should be positive");
        this.maxInputAmperage = maxInputAmperage;
    }

    public int getMaxInputAmperage() {
        return maxInputAmperage;
    }

    public static EnergyContainerConfig of(VoltageTier tier) {
        return of(tier, DEFAULT_CAPACITY_MULTIPLIER);
    }

    public static EnergyConsumerConfig of(VoltageTier tier, long capacityMultiplier) {
        return of(tier, capacityMultiplier, DEFAULT_MAX_INPUT_AMPERAGE);
    }

    public static EnergyConsumerConfig of(VoltageTier tier, long capacityMultiplier, int maxInputAmperage) {
        long capacity = tier.getVoltage() * capacityMultiplier;
        return new EnergyConsumerConfig(capacity, tier, maxInputAmperage);
    }

    public static class Builder extends EnergyContainerConfig.Builder {

        protected int maxInputAmperage = DEFAULT_MAX_INPUT_AMPERAGE;

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

        public Builder maxInputAmperage(int maxInputAmperage) {
            Preconditions.checkArgument(maxInputAmperage > 0, "maxInputAmperage should be positive");
            this.maxInputAmperage = maxInputAmperage;
            return this;
        }

        public Builder defaultCapacity() {
            return capacityMultiplier(DEFAULT_CAPACITY_MULTIPLIER);
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            super.copyPropertiesFrom(builder);
            if (builder instanceof Builder other) {
                this.maxInputAmperage = other.maxInputAmperage;
            }
        }

        @Override
        public EnergyConsumerConfig build() {
            return new EnergyConsumerConfig(this.capacity, this.tier, this.maxInputAmperage);
        }
    }
}
