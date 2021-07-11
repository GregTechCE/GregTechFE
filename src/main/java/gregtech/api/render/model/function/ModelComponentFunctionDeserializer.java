package gregtech.api.render.model.function;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonHelper;

public class ModelComponentFunctionDeserializer {

    public static ModelComponentFunction deserialize(JsonObject object) {
        String functionType = JsonHelper.getString(object, "type");

        if (functionType.equals("apply_orientation")) {
            String facingProperty = JsonHelper.getString(object, "property");
            return new ApplyOrientationFunction(facingProperty);
        }

        throw new JsonParseException("Unknown model component function type: " + functionType);
    }
}
