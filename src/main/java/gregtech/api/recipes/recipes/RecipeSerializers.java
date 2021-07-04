package gregtech.api.recipes.recipes;

import gregtech.api.GTValues;
import gregtech.api.recipes.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RecipeSerializers {

    public static final RecipeSerializer<?> BASIC_MACHINE_RECIPE = register("basic_machine_recipe", new BasicMachineRecipe.Serializer());
    public static final RecipeSerializer<?> ELECTRIC_MACHINE_RECIPE = register("electric_machine_recipe", new ElectricMachineRecipe.Serializer());
    public static final RecipeSerializer<?> BLAST_FURNACE_RECIPE = register("blast_furnace_recipe", new BlastFurnaceRecipe.Serializer());
    public static final RecipeSerializer<?> FLUID_CANNING_RECIPE = register("fluid_canning_recipe", new FluidCanningRecipe.Serializer());
    public static final RecipeSerializer<?> GENERATOR_FUEL_RECIPE = register("generator_fuel_recipe", new GeneratorFuelRecipe.Serializer());
    public static final RecipeSerializer<?> PRIMITIVE_BLAST_FURNACE_RECIPE = register("primitive_blast_furnace_recipe", new PBFRecipe.Serializer());

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void register() {
        BASIC_MACHINE_RECIPE.getClass();
    }

    private static RecipeSerializer<?> register(String name, RecipeSerializer<?> recipeSerializer) {
        return Registry.register(RecipeSerializer.REGISTRY, new Identifier(GTValues.MODID, name), recipeSerializer);
    }
}
