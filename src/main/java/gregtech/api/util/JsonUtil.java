package gregtech.api.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.registry.Registry;

public class JsonUtil {

    public static JsonElement writeItemStack(ItemStack itemStack) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("item", Registry.ITEM.getId(itemStack.getItem()).toString());
        if (itemStack.getCount() > 1) {
            jsonObject.addProperty("count", itemStack.getCount());
        }
        return jsonObject;
    }

    public static ItemStack readItemStack(JsonObject jsonObject) {
        return ShapedRecipe.outputFromJson(jsonObject);
    }
}
