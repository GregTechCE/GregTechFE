package gregtech.api.recipes.builders;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import gregtech.api.recipes.MachineRecipeTypes;
import gregtech.api.recipes.RecipeSerializer;
import gregtech.api.recipes.recipes.RecipeSerializers;
import net.minecraft.util.Identifier;

public class BlastFurnaceRecipeBuilder extends ElectricMachineRecipeBuilder {

    protected int temperature;

    public BlastFurnaceRecipeBuilder(Identifier id) {
        super(id, MachineRecipeTypes.ELECTRIC_BLAST_FURNACE);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        super.copyFrom(otherBuilder);
        if (otherBuilder instanceof BlastFurnaceRecipeBuilder other) {
            this.temperature = other.temperature;
        }
    }

    public BlastFurnaceRecipeBuilder temperature(int temperature) {
        Preconditions.checkArgument(temperature > 0, "Temperature should be positive");
        this.temperature = temperature;
        return this;
    }

    @Override
    protected RecipeSerializer<?> getRecipeSerializer() {
        return RecipeSerializers.BLAST_FURNACE_RECIPE;
    }

    @Override
    protected void addExtraBasicRecipeProperties(JsonObject jsonObject) {
        super.addExtraBasicRecipeProperties(jsonObject);
        Preconditions.checkArgument(this.temperature > 0, "Temperature not set on the recipe builder");
        jsonObject.addProperty("temperature", this.temperature);
    }
}
