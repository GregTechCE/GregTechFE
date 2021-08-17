package gregtech.api.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.module.MachineModuleConfig;
import gregtech.api.util.VoltageTier;

public class EnergyContainerConfig implements MachineModuleConfig {

    private final long capacity;
    private final VoltageTier tier;

    protected EnergyContainerConfig(long capacity, VoltageTier tier) {
        Preconditions.checkNotNull(tier, "tier");
        Preconditions.checkArgument(capacity >= tier.getVoltage(), "capacity >= tier.getVoltage()");
        this.capacity = capacity;
        this.tier = tier;
    }

    public long getCapacity() {
        return capacity;
    }

    public VoltageTier getTier() {
        return tier;
    }

    public static class Builder extends ConfigurationBuilder {

        protected VoltageTier tier;
        protected long capacity;

        public static Builder start() {
            return new Builder();
        }

        public Builder tier(VoltageTier tier) {
            Preconditions.checkNotNull(tier, "tier");
            this.tier = tier;
            return this;
        }

        public Builder capacity(long capacity) {
            Preconditions.checkArgument(capacity > 0, "capacity must be positive");
            this.capacity = capacity;
            return this;
        }

        public Builder capacityMultiplier(int capacityMultiplier) {
            Preconditions.checkArgument(capacityMultiplier > 0, "capacityMultiplier must be positive");
            Preconditions.checkNotNull(this.tier, "tier must be set before calling capacityMultiplier");

            return capacity((long) this.tier.getVoltage() * capacityMultiplier);
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            if (builder instanceof Builder other) {
                this.tier = other.tier;
                this.capacity = other.capacity;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public EnergyContainerConfig build() {
            return new EnergyContainerConfig(this.capacity, this.tier);
        }
    }
}
