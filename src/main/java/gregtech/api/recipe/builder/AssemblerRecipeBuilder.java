package gregtech.api.recipe.builder;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.common.base.Preconditions;
import gregtech.api.fluid.MaterialFluidRegistry;
import gregtech.api.recipe.MachineRecipeTypes;
import gregtech.api.unification.Materials;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

public class AssemblerRecipeBuilder extends ElectricMachineRecipeBuilder {

    protected boolean addSolderingFluidToRecipe;
    protected FluidAmount solderingFluidAmount;

    public AssemblerRecipeBuilder(Identifier id) {
        super(id, MachineRecipeTypes.ASSEMBLING_MACHINE);
    }

    @Override
    public void copyFrom(MachineRecipeBuilder otherBuilder) {
        super.copyFrom(otherBuilder);
        if (otherBuilder instanceof AssemblerRecipeBuilder other) {
            this.addSolderingFluidToRecipe = other.addSolderingFluidToRecipe;
            this.solderingFluidAmount = other.solderingFluidAmount;
        }
    }

    public AssemblerRecipeBuilder addSolderingFluidToRecipe(FluidAmount solderingFluidAmount) {
        Preconditions.checkNotNull(solderingFluidAmount, "solderingFluidAmount");
        Preconditions.checkArgument(!solderingFluidAmount.isPositive(), "Soldering Fluid Amount should be positive");

        this.addSolderingFluidToRecipe = true;
        this.solderingFluidAmount = solderingFluidAmount;
        return this;
    }

    private MachineRecipeBuilder createSolderingFluidRecipe(String postfix, FluidVolume solderingFluid) {
        Identifier recipeId = new Identifier(this.id.getNamespace(), this.id.getPath() + "_" + postfix);

        ElectricMachineRecipeBuilder variantBuilder = new ElectricMachineRecipeBuilder(recipeId, this.recipeType);
        variantBuilder.copyFrom(this);
        return variantBuilder.fluidInput(solderingFluid);
    }

    @Override
    public Stream<MachineRecipeBuilder> flatMapBuilder() {
        if (this.addSolderingFluidToRecipe) {
            FluidAmount solderingAlloyAmount = this.solderingFluidAmount;
            FluidAmount tinAmount = this.solderingFluidAmount.mul(FluidAmount.of(3, 2));
            FluidAmount leadAmount = this.solderingFluidAmount.mul(2);

            FluidVolume solderingFluid = MaterialFluidRegistry.INSTANCE.getMaterialFluid(Materials.SolderingAlloy, solderingAlloyAmount);
            FluidVolume tinFluid = MaterialFluidRegistry.INSTANCE.getMaterialFluid(Materials.Tin, tinAmount);
            FluidVolume leadFluid = MaterialFluidRegistry.INSTANCE.getMaterialFluid(Materials.Lead, leadAmount);

            MachineRecipeBuilder solderingAlloyRecipe = createSolderingFluidRecipe("soldering_alloy", solderingFluid);
            MachineRecipeBuilder tinRecipe = createSolderingFluidRecipe("tin", tinFluid);
            MachineRecipeBuilder leadRecipe = createSolderingFluidRecipe("lead", leadFluid);

            return Stream.of(solderingAlloyRecipe, tinRecipe, leadRecipe);
        }
        return super.flatMapBuilder();
    }
}
