package gregtech.api.unification.material.properties;

public class FluidProperties {

    public static final int DEFAULT_TEMPERATURE = 300;
    public static final int DEFAULT_MELTED_METAL_TEMPERATURE = 1265;

    private final int temperature;
    private final boolean isGaseous;

    private FluidProperties(int temperature, boolean isGaseous) {
        this.temperature = temperature;
        this.isGaseous = isGaseous;
    }

    public static FluidProperties fluid(int temperature) {
        return new FluidProperties(temperature, false);
    }

    public static FluidProperties gas(int temperature) {
        return new FluidProperties(temperature, true);
    }

    public int getTemperature() {
        return temperature;
    }

    public boolean isGaseous() {
        return isGaseous;
    }
}
