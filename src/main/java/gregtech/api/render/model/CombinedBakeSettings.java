package gregtech.api.render.model;

import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.util.math.AffineTransformation;

public class CombinedBakeSettings implements ModelBakeSettings {

    private final ModelBakeSettings original;
    private final AffineTransformation rotation;
    private final boolean uvLock;

    public CombinedBakeSettings(ModelBakeSettings original, AffineTransformation rotation, boolean uvLock) {
        this.original = original;
        this.rotation = rotation;
        this.uvLock = uvLock;
    }

    @Override
    public AffineTransformation getRotation() {
        return this.rotation.multiply(this.original.getRotation());
    }

    @Override
    public boolean isUvLocked() {
        return original.isUvLocked() || this.uvLock;
    }
}
