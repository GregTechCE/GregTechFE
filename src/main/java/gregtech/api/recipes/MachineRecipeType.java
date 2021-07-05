package gregtech.api.recipes;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import gregtech.api.recipes.context.RecipeContext;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MachineRecipeType {

    public static final Registry<MachineRecipeType> REGISTRY = FabricRegistryBuilder
            .createSimple(MachineRecipeType.class, new Identifier(GTValues.MODID, "recipe_maps"))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    private final Class<? extends RecipeContext> contextClass;
    private final RecipeType<?> vanillaRecipeType;

    public MachineRecipeType(Settings settings) {
        Preconditions.checkNotNull(settings.contextClass, "contextClass is not set");

        this.contextClass = settings.contextClass;
        this.vanillaRecipeType = settings.vanillaRecipeType;
    }

    public Identifier getName() {
        return Preconditions.checkNotNull(REGISTRY.getId(this), "RecipeMap not registered");
    }

    public Class<? extends RecipeContext> getContextClass() {
        return contextClass;
    }

    public RecipeType<?> getVanillaRecipeType() {
        return vanillaRecipeType;
    }

    @Override
    public String toString() {
        return String.valueOf(REGISTRY.getId(this));
    }

    public static class Settings {
        private Class<? extends RecipeContext> contextClass = RecipeContext.class;
        private RecipeType<?> vanillaRecipeType;

        public Settings contextClass(Class<? extends RecipeContext> contextClass) {
            Preconditions.checkNotNull(contextClass, "contextClass");
            Preconditions.checkArgument(RecipeContext.class.isAssignableFrom(contextClass), "contextClass does not extend RecipeContext");

            this.contextClass = contextClass;
            return this;
        }

        public Settings vanillaRecipeType(RecipeType<?> recipeType) {
            Preconditions.checkNotNull(recipeType, "recipeType");
            this.vanillaRecipeType = recipeType;
            return this;
        }
    }
}
