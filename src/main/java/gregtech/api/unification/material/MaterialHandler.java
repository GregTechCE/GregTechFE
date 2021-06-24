package gregtech.api.unification.material;

/**
 * Use this to register your own materials in the right stage before material registry freezing
 * You should implement this interface and register your implementation in mod descriptor file
 * <p>
 * {@link MaterialHandler#onMaterialsInit()} will be called by GTCE in PreInit just before
 * material registry freezing after registration of built-in materials and before
 * running early material-manipulating CraftTweaker scripts.
 */
public interface MaterialHandler {

    void onMaterialsInit();
}
