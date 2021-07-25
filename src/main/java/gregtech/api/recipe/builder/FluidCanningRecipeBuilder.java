package gregtech.api.recipe.builder;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import gregtech.api.recipe.MachineRecipeType;
import gregtech.api.recipe.RecipeSerializer;
import gregtech.api.recipe.type.RecipeSerializers;
import net.minecraft.util.Identifier;

public class FluidCanningRecipeBuilder extends AbstractMachineRecipeBuilder {

    protected int duration;
    protected int recipeEUt;

    public FluidCanningRecipeBuilder(Identifier id, MachineRecipeType recipeType) {
        super(id, recipeType);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        if (otherBuilder instanceof FluidCanningRecipeBuilder other) {
            this.duration = other.duration;
            this.recipeEUt = other.recipeEUt;
        }
    }

    public FluidCanningRecipeBuilder duration(int duration) {
        Preconditions.checkArgument(duration > 0, "duration should be positive");
        this.duration = duration;
        return this;
    }

    public FluidCanningRecipeBuilder EUt(int EUt) {
        Preconditions.checkArgument(EUt > 0, "EUt should be positive");
        this.recipeEUt = EUt;
        return this;
    }

    @Override
    protected RecipeSerializer<?> getRecipeSerializer() {
        return RecipeSerializers.FLUID_CANNING_RECIPE;
    }

    @Override
    protected void writeRecipeTypeData(JsonObject jsonObject) {
        Preconditions.checkArgument(this.duration > 0, "Duration is not set");
        Preconditions.checkArgument(this.recipeEUt > 0, "RecipeEUt is not set");

        jsonObject.addProperty("duration", this.duration);
        jsonObject.addProperty("eu_per_tick", this.recipeEUt);
    }
}
