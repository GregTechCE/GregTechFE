package gregtech.api.fluid.properties;

public class StandardMaterialFluidProperties {

    public static final MaterialFluidProperties DEFAULT_FLUID = new MaterialFluidProperties(
            new MaterialFluidProperties.Settings()
    );

    public static final MaterialFluidProperties DEFAULT_GAS = new MaterialFluidProperties(
            new MaterialFluidProperties.Settings()
                .gaseous()
    );

    public static final MaterialFluidProperties MOLTEN_FLUID = new MaterialFluidProperties(
            new MaterialFluidProperties.Settings()
                .fluidNameTemplate("molten_{material}")
                .translationKey("fluid.gregtech.molten_fluid")
                .temperature(MaterialFluidProperties.MOLTEN_FLUID_TEMPERATURE)
                .luminosity(15)
                .blastResistance(300.0f)
                .levelDecreasePerBlock(3).netherLevelDecreasePerBlock(2)
                .flowSpeed(2).netherFlowSpeed(3)
                .fluidTickRate(30).netherFluidTickRate(10)
    );
}
