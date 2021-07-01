package gregtech.api.unification.util;

import net.minecraft.util.Identifier;

public class MaterialIconSet {

    private final Identifier modelSetPath;

    /**
     * @param modelSetPath path to the folder with item models, relative to
     *                  assets/[namespace]/models/items and without a trailing slash
     */
    public MaterialIconSet(Identifier modelSetPath) {
        this.modelSetPath = modelSetPath;
    }

    public Identifier getModelLocation(String modelType) {
        return new Identifier(modelSetPath.getNamespace(), modelSetPath.getPath() + "/" + modelType);
    }
}
