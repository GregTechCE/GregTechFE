package gregtech.api.recipe.builder;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import gregtech.api.recipe.MachineRecipeType;
import gregtech.api.recipe.RecipeSerializer;
import gregtech.api.recipe.type.RecipeSerializers;
import net.minecraft.util.Identifier;

public class ElectricMachineRecipeBuilder extends BasicMachineRecipeBuilder {

    protected int recipeEUt;

    public ElectricMachineRecipeBuilder(Identifier id, MachineRecipeType recipeType) {
        super(id, recipeType);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        super.copyFrom(otherBuilder);
        if (otherBuilder instanceof ElectricMachineRecipeBuilder other) {
            this.recipeEUt = other.recipeEUt;
        }
    }

    public ElectricMachineRecipeBuilder EUt(int recipeEUt) {
        Preconditions.checkArgument(recipeEUt > 0, "recipeEUt should be positive");
        this.recipeEUt = recipeEUt;
        return this;
    }

    @Override
    protected RecipeSerializer<?> getRecipeSerializer() {
        return RecipeSerializers.ELECTRIC_MACHINE_RECIPE;
    }

    @Override
    protected void addExtraBasicRecipeProperties(JsonObject jsonObject) {
        Preconditions.checkArgument(this.recipeEUt > 0, "RecipeEUt not set in recipe builder");
        jsonObject.addProperty("eu_per_tick", this.recipeEUt);
    }
}
