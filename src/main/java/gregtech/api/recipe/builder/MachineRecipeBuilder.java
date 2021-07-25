package gregtech.api.recipe.builder;

import com.google.gson.JsonObject;
import gregtech.api.recipe.MachineRecipeType;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

public interface MachineRecipeBuilder {

    /**
     * Allows generating multiple recipes from a single recipe builder on registration
     * Keep in mind this recipe WILL NOT be added additionally, so you should
     * return this object in the stream if you want it added too
     */
    default Stream<MachineRecipeBuilder> flatMapBuilder() {
        return Stream.of(this);
    }

    void copyFrom(MachineRecipeBuilder other);

    Identifier getId();

    MachineRecipeType getRecipeType();

    JsonObject buildRecipeObject();
}
