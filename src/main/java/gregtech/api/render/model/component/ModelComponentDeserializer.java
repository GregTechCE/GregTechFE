package gregtech.api.render.model.component;

import com.google.gson.JsonObject;
import gregtech.api.render.model.function.ModelComponentFunction;
import gregtech.api.render.model.function.ModelComponentFunctionDeserializer;
import gregtech.api.render.model.part.ModelPartDeserializer;
import gregtech.api.render.model.part.ModelPartFactory;
import gregtech.api.render.model.selector.ModelPartSelector;
import gregtech.api.render.model.selector.PartSelectorDeserializer;
import net.minecraft.util.JsonHelper;

public class ModelComponentDeserializer {

    public static ModelComponentTemplate deserialize(JsonObject object) {
        ModelPartSelector partSelector = ModelPartSelector.TRUE;

        if (object.has("when")) {
            JsonObject selectorObject = JsonHelper.getObject(object, "when");
            partSelector = PartSelectorDeserializer.deserializeSelector(selectorObject);
        }

        JsonObject partObject = JsonHelper.getObject(object, "apply");
        ModelPartFactory modelPart = ModelPartDeserializer.deserialize(partObject);

        ModelComponentFunction function = ModelComponentFunction.IDENTITY;
        boolean isStatic = JsonHelper.getBoolean(object, "static", false);

        if (object.has("function")) {
            JsonObject functionObject = JsonHelper.getObject(object, "function");
            function = ModelComponentFunctionDeserializer.deserialize(functionObject);
        }
        return new ModelComponentTemplate(modelPart, partSelector, function, isStatic);
    }
}
