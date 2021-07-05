package gregtech.api.recipes;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.item.FixedItemInvView;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import gregtech.api.recipes.context.RecipeContext;
import gregtech.api.recipes.recipes.VanillaRecipeWrappers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RecipeMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeMap.class);

    private final MachineRecipeType recipeType;

    private final List<MachineRecipe<?>> recipes = new ArrayList<>();

    private final Map<Item, List<MachineRecipe<?>>> recipesByInputs = new HashMap<>();
    private final Map<FluidKey, List<MachineRecipe<?>>> recipesByFluidInputs = new HashMap<>();
    private final List<MachineRecipe<?>> uncacheableRecipes = new ArrayList<>();

    public RecipeMap(MachineRecipeType recipeType) {
        Preconditions.checkNotNull(recipeType, "recipeType");
        this.recipeType = recipeType;
    }

    public MachineRecipeType getRecipeType() {
        return recipeType;
    }

    @SuppressWarnings("unchecked")
    private static boolean recipeMatches(RecipeContext context, MachineRecipe<?> recipe) {
        return ((MachineRecipe<RecipeContext>) recipe).matches(context);
    }

    public List<MachineRecipe<?>> getRecipes() {
        return ImmutableList.copyOf(this.recipes);
    }

    public MachineRecipe<?> findRecipe(RecipeContext recipeContext) {
        Class<?> contextClass = recipeType.getContextClass();
        Preconditions.checkArgument(contextClass.isInstance(recipeContext),
                "Context has invalid type for the provided recipe map");

        HashSet<MachineRecipe<?>> lookedUpRecipes = new HashSet<>();

        //Lookup item recipes first by checking cached recipes by input item type
        FixedItemInvView itemInvView = recipeContext.getItemInventory();
        for (int slotIndex = 0; slotIndex < itemInvView.getSlotCount(); slotIndex++) {
            ItemStack itemStack = itemInvView.getInvStack(slotIndex);

            if (itemStack.isEmpty()) {
                continue;
            }
            List<MachineRecipe<?>> itemRecipes = this.recipesByInputs.get(itemStack.getItem());

            for (MachineRecipe<?> itemRecipe : itemRecipes) {
                if (lookedUpRecipes.contains(itemRecipe)) {
                    continue;
                }
                if (recipeMatches(recipeContext, itemRecipe)) {
                    return itemRecipe;
                }
                lookedUpRecipes.add(itemRecipe);
            }
        }

        //Then try to look up recipes by fluids used in them
        FixedFluidInv fluidInvView = recipeContext.getFluidInventory();
        for (int tankIndex = 0; tankIndex < fluidInvView.getTankCount(); tankIndex++) {
            FluidVolume fluidVolume = fluidInvView.getInvFluid(tankIndex);

            if (fluidVolume.isEmpty()) {
                continue;
            }
            List<MachineRecipe<?>> fluidRecipes = this.recipesByFluidInputs.get(fluidVolume.getFluidKey());

            for (MachineRecipe<?> fluidRecipe : fluidRecipes) {
                if (lookedUpRecipes.contains(fluidRecipe)) {
                    continue;
                }
                if (recipeMatches(recipeContext, fluidRecipe)) {
                    return fluidRecipe;
                }
                lookedUpRecipes.add(fluidRecipe);
            }
        }

        //If static recipe lookup failed, we try to match dynamic recipes
        for (MachineRecipe<?> dynamicRecipe : this.uncacheableRecipes) {
            if (recipeMatches(recipeContext, dynamicRecipe)) {
                return dynamicRecipe;
            }
        }
        return null;
    }

    public void clearRecipes() {
        this.recipes.clear();

        this.recipesByInputs.clear();
        this.recipesByFluidInputs.clear();
        this.uncacheableRecipes.clear();
    }

    private void addAndCacheRecipe(MachineRecipe<?> recipe) {
        this.recipes.add(recipe);

        if (recipe instanceof CacheableMachineRecipe cacheableMachineRecipe && cacheableMachineRecipe.canBeCached()) {
            for (Item item : cacheableMachineRecipe.getReferencedItems()) {
                List<MachineRecipe<?>> recipes = this.recipesByInputs.computeIfAbsent(item, k -> new ArrayList<>());
                recipes.add(recipe);
            }

            for (FluidKey fluid : cacheableMachineRecipe.getReferencedFluids()) {
                List<MachineRecipe<?>> recipes = this.recipesByFluidInputs.computeIfAbsent(fluid, k -> new ArrayList<>());
                recipes.add(recipe);
            }
        } else {
            this.uncacheableRecipes.add(recipe);
        }
    }

    public void applyVanillaRecipes(RecipeManager recipeManager) {
        RecipeType<?> vanillaRecipeType = this.recipeType.getVanillaRecipeType();
        if (vanillaRecipeType != null) {
            List<MachineRecipe<?>> vanillaRecipes = VanillaRecipeWrappers
                    .mirrorVanillaRecipes(recipeManager, vanillaRecipeType);
            appendRecipes(vanillaRecipes);
            LOGGER.info("Loaded {} vanilla recipes of type {} into recipe map {}",
                    vanillaRecipes.size(), vanillaRecipeType, this);
        }
    }

    public void appendRecipes(List<MachineRecipe<?>> recipes) {
        Class<?> contextClass = this.recipeType.getContextClass();
        for (MachineRecipe<?> machineRecipe : recipes) {
            Class<?> minimumClass = machineRecipe.getMinimumSupportedContextClass();

            if (!minimumClass.isAssignableFrom(contextClass)) {
                LOGGER.error("Failed to add recipe {} to recipe map {}, recipes required context class {}, but recipe map only supports {}",
                        machineRecipe.getId(), this, minimumClass.getSimpleName(), contextClass.getSimpleName());
                continue;
            }
            addAndCacheRecipe(machineRecipe);
        }
    }
}
