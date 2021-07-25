package gregtech.api.recipe.builder;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import gregtech.api.recipe.MachineRecipeType;
import gregtech.api.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public abstract class AbstractMachineRecipeBuilder implements MachineRecipeBuilder {

    protected final Identifier id;
    protected final MachineRecipeType recipeType;

    public AbstractMachineRecipeBuilder(Identifier id, MachineRecipeType recipeType) {
        Preconditions.checkNotNull(id, "id");
        Preconditions.checkNotNull(recipeType, "recipeType");
        this.id = id;
        this.recipeType = recipeType;
    }

    @Override
    public final Identifier getId() {
        return id;
    }

    @Override
    public final MachineRecipeType getRecipeType() {
        return recipeType;
    }

    protected abstract RecipeSerializer<?> getRecipeSerializer();

    protected abstract void writeRecipeTypeData(JsonObject jsonObject);

    @Override
    public final JsonObject buildRecipeObject() {
        JsonObject jsonObject = new JsonObject();

        Identifier typeId = RecipeSerializer.REGISTRY.getId(getRecipeSerializer());
        jsonObject.addProperty("type", Preconditions.checkNotNull(typeId).toString());

        writeRecipeTypeData(jsonObject);
        return jsonObject;
    }
}
