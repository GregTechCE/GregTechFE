package gregtech.api.render.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gregtech.api.render.model.component.ModelComponentDeserializer;
import gregtech.api.render.model.component.ModelComponentTemplate;
import net.minecraft.util.JsonHelper;

public class StateModelDeserializer {

    public static StateModelTemplate deserialize(JsonObject object) {
        JsonArray componentsArray = JsonHelper.getArray(object, "components");

        ImmutableList.Builder<ModelComponentTemplate> components = ImmutableList.builder();
        for (JsonElement arrayElement : componentsArray) {
            components.add(ModelComponentDeserializer.deserialize(arrayElement.getAsJsonObject()));
        }
        return new StateModelTemplate(components.build());
    }
}
