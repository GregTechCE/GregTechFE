package gregtech.api.unification.material.flags;

public class MaterialFlags {

    public static final MaterialFlag GENERATE_PLATE = (new MaterialFlag.Builder("GENERATE_PLATE").build());
    public static final MaterialFlag GENERATE_FOIL = (new MaterialFlag.Builder("GENERATE_FOIL").requires(GENERATE_PLATE).build());


    private MaterialFlags() {

    }
}
