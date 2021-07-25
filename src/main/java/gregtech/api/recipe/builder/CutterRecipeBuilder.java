package gregtech.api.recipe.builder;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import gregtech.api.fluid.MaterialFluidRegistry;
import gregtech.api.recipe.MachineRecipeTypes;
import gregtech.api.unification.Materials;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

public class CutterRecipeBuilder extends ElectricMachineRecipeBuilder {

    protected boolean addLubricantToRecipe;

    public CutterRecipeBuilder(Identifier id) {
        super(id, MachineRecipeTypes.CUTTING_SAW);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        super.copyFrom(otherBuilder);
        if (otherBuilder instanceof CutterRecipeBuilder other) {
            this.addLubricantToRecipe = other.addLubricantToRecipe;
        }
    }

    public CutterRecipeBuilder addLubricantToRecipe() {
        this.addLubricantToRecipe = true;
        return this;
    }

    private MachineRecipeBuilder createLubricantRecipe(String postfix, FluidKey lubricant, int minFluidAmount, int maxFluidAmount, int divisor) {
        Identifier recipeId = new Identifier(this.id.getNamespace(), this.id.getPath() + "_" + postfix);

        int rawFluidAmount = Math.max(minFluidAmount, Math.min(maxFluidAmount, duration * recipeEUt / divisor));
        FluidAmount fluidAmount = FluidAmount.of(rawFluidAmount, 1000);

        ElectricMachineRecipeBuilder variantBuilder = new ElectricMachineRecipeBuilder(recipeId, this.recipeType);
        variantBuilder.copyFrom(this);
        return variantBuilder.fluidInput(lubricant.withAmount(fluidAmount));
    }

    @Override
    public Stream<MachineRecipeBuilder> flatMapBuilder() {
        if (this.addLubricantToRecipe) {
            FluidKey distilledWater = MaterialFluidRegistry.INSTANCE.getMaterialFluid(Materials.DistilledWater);
            FluidKey lubricant = MaterialFluidRegistry.INSTANCE.getMaterialFluid(Materials.Lubricant);

            MachineRecipeBuilder waterRecipe = createLubricantRecipe("water", FluidKeys.WATER, 4, 1000, 320);
            MachineRecipeBuilder distilledWaterRecipe = createLubricantRecipe("distilled_water", distilledWater, 3, 750, 426);
            MachineRecipeBuilder lubricantRecipe = createLubricantRecipe("lubricant", lubricant, 1, 250, 1280);

            return Stream.of(waterRecipe, distilledWaterRecipe, lubricantRecipe);
        }
        return super.flatMapBuilder();
    }
}
