package gregtech.api.unification.material.properties;

public class FluidProperties {

    public static final int DEFAULT_TEMPERATURE = 300;
    public static final int DEFAULT_MELTED_METAL_TEMPERATURE = 1265;

    private final int temperature;
    private final boolean isGaseous;
    private final boolean generatePlasma;

    private FluidProperties(int temperature, boolean isGaseous, boolean generatePlasma) {
        this.temperature = temperature;
        this.isGaseous = isGaseous;
        this.generatePlasma = generatePlasma;
    }

    public static FluidProperties fluid(int temperature, boolean generatePlasma) {
        return new FluidProperties(temperature, false, generatePlasma);
    }

    public static FluidProperties gas(int temperature, boolean generatePlasma) {
        return new FluidProperties(temperature, true, generatePlasma);
    }

    public int getTemperature() {
        return temperature;
    }

    public boolean isGaseous() {
        return isGaseous;
    }

    public boolean shouldGeneratePlasma() {
        return generatePlasma;
    }
}
