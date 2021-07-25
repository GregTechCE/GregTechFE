package gregtech.api.recipe.builder;

import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import gregtech.api.recipe.MachineRecipeType;
import gregtech.api.recipe.RecipeSerializer;
import gregtech.api.recipe.type.RecipeSerializers;
import net.minecraft.util.Identifier;

public class GeneratorFuelRecipeBuilder extends AbstractMachineRecipeBuilder {

    protected FluidVolume fuelFluid = FluidVolumeUtil.EMPTY;
    protected int duration;
    protected int generatedEUt;

    public GeneratorFuelRecipeBuilder(Identifier id, MachineRecipeType recipeType) {
        super(id, recipeType);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        if (otherBuilder instanceof GeneratorFuelRecipeBuilder other) {
            this.fuelFluid = other.fuelFluid;
            this.duration = other.duration;
            this.generatedEUt = other.generatedEUt;
        }
    }

    public GeneratorFuelRecipeBuilder fuel(FluidVolume fuelFluid) {
        Preconditions.checkArgument(!fuelFluid.isEmpty(), "Empty FluidVolume was provided");
        this.fuelFluid = fuelFluid.copy();
        return this;
    }

    public GeneratorFuelRecipeBuilder duration(int duration) {
        Preconditions.checkArgument(duration > 0, "duration should be positive");
        this.duration = duration;
        return this;
    }

    public GeneratorFuelRecipeBuilder EUt(int EUt) {
        Preconditions.checkArgument(EUt > 0, "EUt should be positive");
        this.generatedEUt = EUt;
        return this;
    }

    @Override
    protected RecipeSerializer<?> getRecipeSerializer() {
        return RecipeSerializers.GENERATOR_FUEL_RECIPE;
    }

    @Override
    protected void writeRecipeTypeData(JsonObject jsonObject) {
        Preconditions.checkArgument(!this.fuelFluid.isEmpty(), "FuelFluid is not set");
        Preconditions.checkArgument(this.duration > 0, "Duration is not set");
        Preconditions.checkArgument(this.generatedEUt > 0, "GeneratedEUt is not set");

        jsonObject.add("fuel", this.fuelFluid.toJson());
        jsonObject.addProperty("duration", this.duration);
        jsonObject.addProperty("eu_per_tick", this.generatedEUt);
    }
}
