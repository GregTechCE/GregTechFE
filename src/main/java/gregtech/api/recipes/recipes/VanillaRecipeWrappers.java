package gregtech.api.recipes.recipes;

import gregtech.api.recipes.MachineRecipe;
import gregtech.api.recipes.util.CountableIngredient;
import gregtech.api.recipes.context.ElectricMachineContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VanillaRecipeWrappers {

    private static final Logger LOGGER = LoggerFactory.getLogger(VanillaRecipeWrappers.class);
    private static final int VANILLA_COOKING_RECIPE_EU_PER_TICK = 4;

    public static <C extends ElectricMachineContext> List<MachineRecipe<C>> mirrorVanillaRecipes(RecipeManager recipeManager, RecipeType<? extends Recipe<?>> recipeType) {
        List<? extends Recipe<?>> allRecipes = recipeManager.listAllOfType(recipeType);
        ArrayList<MachineRecipe<C>> convertedRecipes = new ArrayList<>();

        for (Recipe<?> recipe : allRecipes) {
            MachineRecipe<C> mappedRecipe = convertRecipe(recipe);
            if (mappedRecipe == null) {
                LOGGER.warn("Failed to map vanilla recipe class {} ({}) to MachineRecipe equivalent", recipe.getClass(), recipe.getId());
                continue;
            }
            convertedRecipes.add(mappedRecipe);
        }
        return convertedRecipes;
    }

    @Nullable
    private static <C extends ElectricMachineContext> MachineRecipe<C> convertRecipe(Recipe<?> recipe) {
        if (recipe instanceof AbstractCookingRecipe abstractCookingRecipe) {
            return createCookingRecipeWrapper(abstractCookingRecipe);
        }
        return null;
    }

    private static <C extends ElectricMachineContext> MachineRecipe<C> createCookingRecipeWrapper(AbstractCookingRecipe recipe) {
        DefaultedList<Ingredient> rawIngredients = recipe.getIngredients();

        List<CountableIngredient> recipeInputs = rawIngredients.stream()
                .map(CountableIngredient::from)
                .collect(Collectors.toList());
        ItemStack recipeOutput = recipe.getOutput().copy();
        int recipeDuration = recipe.getCookTime();

        return new ElectricMachineRecipe<>(
                recipeInputs,
                Collections.emptyList(),
                DefaultedList.copyOf(ItemStack.EMPTY, recipeOutput),
                Collections.emptyList(),
                Collections.emptyList(),
                recipeDuration, VANILLA_COOKING_RECIPE_EU_PER_TICK);
    }
}
