package gregtech.api.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.module.MachineModuleConfig;
import gregtech.api.module.MachineModuleType;
import gregtech.api.module.impl.archetype.IOInventoryModule;

public class InventoryTypeConfig implements MachineModuleConfig {

    private final MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType;

    protected InventoryTypeConfig(MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType) {
        Preconditions.checkNotNull(inventoryModuleType, "inventoryModuleType");
        this.inventoryModuleType = inventoryModuleType;
    }

    public MachineModuleType<?, ? extends IOInventoryModule> getInventoryModuleType() {
        return inventoryModuleType;
    }

    public static class Builder extends ConfigurationBuilder {

        protected MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType;

        public static Builder start() {
            return new Builder();
        }

        public Builder inventory(MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType) {
            Preconditions.checkNotNull(inventoryModuleType, "inventoryModuleType");
            this.inventoryModuleType = inventoryModuleType;
            return this;
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            if (builder instanceof Builder other) {
                this.inventoryModuleType = other.inventoryModuleType;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public InventoryTypeConfig build() {
            return new InventoryTypeConfig(this.inventoryModuleType);
        }
    }
}
