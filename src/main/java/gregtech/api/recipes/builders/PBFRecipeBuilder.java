package gregtech.api.recipes.builders;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import gregtech.api.recipes.MachineRecipeTypes;
import gregtech.api.recipes.RecipeSerializer;
import gregtech.api.recipes.recipes.RecipeSerializers;
import net.minecraft.util.Identifier;

public class PBFRecipeBuilder extends BasicMachineRecipeBuilder {

    protected int fuelCost;

    public PBFRecipeBuilder(Identifier id) {
        super(id, MachineRecipeTypes.PRIMITIVE_BLAST_FURNACE);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        super.copyFrom(otherBuilder);
        if (otherBuilder instanceof PBFRecipeBuilder other) {
            this.fuelCost = other.fuelCost;
        }
    }

    public PBFRecipeBuilder fuelCost(int fuelCost) {
        Preconditions.checkArgument(fuelCost > 0, "FuelCost should be positive");
        this.fuelCost = fuelCost;
        return this;
    }

    @Override
    protected RecipeSerializer<?> getRecipeSerializer() {
        return RecipeSerializers.PRIMITIVE_BLAST_FURNACE_RECIPE;
    }

    @Override
    protected void addExtraBasicRecipeProperties(JsonObject jsonObject) {
        Preconditions.checkArgument(this.fuelCost > 0, "FuelCost is not set on the recipe builder");
        jsonObject.addProperty("fuel_cost", this.fuelCost);
    }
}
