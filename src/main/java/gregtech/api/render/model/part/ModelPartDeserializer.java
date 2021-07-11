package gregtech.api.render.model.part;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Vec3f;

import java.util.HashMap;
import java.util.Map;

public class ModelPartDeserializer {

    @SuppressWarnings("deprecation")
    public static ModelPartFactory deserialize(JsonObject object) {
        boolean uvlock = JsonHelper.getBoolean(object, "uvlock", false);

        int xRotation = JsonHelper.getInt(object, "x", 0);
        int yRotation = JsonHelper.getInt(object, "y", 0);
        AffineTransformation rotation = ModelRotation.get(xRotation, yRotation).getRotation();

        if (object.has("scale")) {
            float scale = JsonHelper.getFloat(object, "scale");
            AffineTransformation scaleTransform = new AffineTransformation(null, null, new Vec3f(scale, scale, scale), null);
            rotation = scaleTransform.multiply(rotation);
        }

        if (object.has("model")) {
            Map<String, SpriteIdentifier> textureOverrides = new HashMap<>();

            if (object.has("textures")) {
                JsonObject texturesObject = JsonHelper.getObject(object, "textures");

                for (Map.Entry<String, JsonElement> entry : texturesObject.entrySet()) {
                    String placeholderName = entry.getKey();
                    String spriteIdString = entry.getValue().getAsString();

                    Identifier spriteId = Identifier.tryParse(spriteIdString);
                    if (spriteId == null) {
                        throw new JsonParseException("Failed to parse sprite location for texture placeholder " + placeholderName + ": " + spriteIdString);
                    }

                    SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, spriteId);
                    textureOverrides.put(placeholderName, spriteIdentifier);
                }
            }


            String modelIdString = JsonHelper.getString(object, "model");
            Identifier parentModelId = Identifier.tryParse(modelIdString);

            if (parentModelId == null) {
                throw new JsonParseException("Failed to parse model identifier: " + modelIdString);
            }

            SimpleModelPart componentModel = new SimpleModelPart(parentModelId, rotation, uvlock, textureOverrides);
            return ModelPartFactory.identity(componentModel);
        }

        if (object.has("variant")) {
            String variantIdString = JsonHelper.getString(object, "variant");
            Identifier variantId = Identifier.tryParse(variantIdString);

            if (variantId == null) {
                throw new JsonParseException("Failed to parse variant model identifier: " + variantIdString);
            }
            return new VariantModelPartFactory(variantId, rotation, uvlock);
        }

        throw new JsonParseException("Expected either variant or model field on model component");
    }
}
