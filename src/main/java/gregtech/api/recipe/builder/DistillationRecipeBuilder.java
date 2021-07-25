package gregtech.api.recipe.builder;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import gregtech.api.recipe.MachineRecipeTypes;
import gregtech.common.items.GTItems;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.stream.Stream;

public class DistillationRecipeBuilder extends ElectricMachineRecipeBuilder {

    protected boolean generateDistilleryRecipes;

    public DistillationRecipeBuilder(Identifier id) {
        super(id, MachineRecipeTypes.DISTILLATION_TOWER);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        super.copyFrom(otherBuilder);
        if (otherBuilder instanceof DistillationRecipeBuilder other) {
            this.generateDistilleryRecipes = other.generateDistilleryRecipes;
        }
    }

    public DistillationRecipeBuilder generateDistilleryRecipes() {
        this.generateDistilleryRecipes = true;
        return this;
    }

    private MachineRecipeBuilder createDistilleryRecipe(int fluidOutputIndex) {
        FluidVolume fluidOutput = this.fluidOutputs.get(fluidOutputIndex);

        String recipePostfix = fluidOutput.getFluidKey().entry.getId().getPath();
        Identifier recipeId = new Identifier(this.id.getNamespace(), this.id.getPath() + "_" + recipePostfix);

        ElectricMachineRecipeBuilder recipeBuilder = new ElectricMachineRecipeBuilder(recipeId, MachineRecipeTypes.DISTILLERY);
        recipeBuilder.fluidInputs.addAll(this.fluidInputs);
        recipeBuilder.inputs.addAll(this.inputs);

        return recipeBuilder.EUt(this.recipeEUt / 4)
                .duration(this.duration * 2)
                .notConsumable(GTItems.getIntegratedCircuit(fluidOutputIndex))
                .fluidOutput(fluidOutput);
    }

    @Override
    public Stream<MachineRecipeBuilder> flatMapBuilder() {
        if (this.generateDistilleryRecipes) {
            ArrayList<MachineRecipeBuilder> resultRecipes = new ArrayList<>();
            resultRecipes.add(this);

            for (int i = 0; i < fluidOutputs.size(); i++) {
                resultRecipes.add(createDistilleryRecipe(i));
            }
            return resultRecipes.stream();
        }
        return super.flatMapBuilder();
    }
}
