package gregtech.api.render.model.selector;

import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PartSelectorDeserializer {

    public static ModelPartSelector deserializeSelector(JsonObject object) {
        Set<Map.Entry<String, JsonElement>> entrySet = object.entrySet();

        if (entrySet.isEmpty()) {
            throw new JsonParseException("No elements found in the selector");
        }
        if (entrySet.size() == 1) {
            if (object.has("OR")) {
                JsonArray orArrayElements = JsonHelper.getArray(object, "OR");

                List<ModelPartSelector> orSelectors = Streams.stream(orArrayElements)
                        .map(JsonElement::getAsJsonObject)
                        .map(PartSelectorDeserializer::deserializeSelector)
                        .collect(Collectors.toList());

                return new OrModelPartSelector(orSelectors);

            } else if (object.has("AND")) {
                JsonArray andArrayElements = JsonHelper.getArray(object, "AND");

                List<ModelPartSelector> andSelectors = Streams.stream(andArrayElements)
                        .map(JsonElement::getAsJsonObject)
                        .map(PartSelectorDeserializer::deserializeSelector)
                        .collect(Collectors.toList());

                return new AndModelPartSelector(andSelectors);
            } else {
                return createStatePropertySelector(entrySet.iterator().next());
            }
        }

        List<ModelPartSelector> andSelectors = entrySet.stream()
                .map(PartSelectorDeserializer::createStatePropertySelector)
                .collect(Collectors.toList());

        return new AndModelPartSelector(andSelectors);
    }

    private static ModelPartSelector createStatePropertySelector(Map.Entry<String, JsonElement> entry) {
        return new SimpleModelPartSelector(entry.getKey(), entry.getValue().getAsString());
    }
}
