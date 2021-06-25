package gregtech.api.unification.material.properties;

public class MaterialProperty<T> {
    private final String name;
    private final T propertyValue;

    public MaterialProperty(String name, T propertyValue){
        this.name = name;
        this.propertyValue = propertyValue;
    }
}
