package gregtech.api.block.machine.module.impl.config;

import com.google.common.base.Preconditions;
import gregtech.api.block.machine.module.MachineModuleType;
import gregtech.api.block.machine.module.impl.archetype.IOInventoryModule;
import gregtech.api.recipe.MachineRecipeType;

public class ManufacturerConfig extends InventoryTypeConfig {

    private final MachineRecipeType recipeType;

    public ManufacturerConfig(MachineModuleType<?, ? extends IOInventoryModule> inventoryModule, MachineRecipeType recipeType) {
        super(inventoryModule);
        Preconditions.checkNotNull(recipeType, "recipeType");
        this.recipeType = recipeType;
    }

    public MachineRecipeType getRecipeType() {
        return recipeType;
    }

    public static class Builder extends InventoryTypeConfig.Builder {

        protected MachineRecipeType recipeType;

        public static Builder start() {
            return new Builder();
        }

        public Builder recipeType(MachineRecipeType recipeType) {
            Preconditions.checkNotNull(recipeType, "recipeType");
            this.recipeType = recipeType;
            return this;
        }

        @Override
        public Builder inventory(MachineModuleType<?, ? extends IOInventoryModule> inventoryModuleType) {
            return (Builder) super.inventory(inventoryModuleType);
        }

        @Override
        protected void copyPropertiesFrom(ConfigurationBuilder builder) {
            super.copyPropertiesFrom(builder);
            if (builder instanceof Builder other) {
                this.recipeType = other.recipeType;
            }
        }

        @Override
        public Builder copy() {
            Builder builder = new Builder();
            builder.copyPropertiesFrom(this);
            return builder;
        }

        @Override
        public ManufacturerConfig build() {
            return new ManufacturerConfig(this.inventoryModuleType, this.recipeType);
        }
    }
}
