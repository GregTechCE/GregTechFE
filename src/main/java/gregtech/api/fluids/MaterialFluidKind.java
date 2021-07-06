package gregtech.api.fluids;

import gregtech.api.unification.material.properties.FluidProperties;

public enum MaterialFluidKind {
    FLUID("fluid", "{material}", true),
    PLASMA("plasma", "{material}_plasma", false);

    private final String registryName;
    private final String fluidNameTemplate;
    private final boolean generateFluidBlock;

    MaterialFluidKind(String registryName, String fluidNameTemplate, boolean generateFluidBlock) {
        this.registryName = registryName;
        this.fluidNameTemplate = fluidNameTemplate;
        this.generateFluidBlock = generateFluidBlock;
    }

    public String getRegistryName() {
        return registryName;
    }

    public String createFluidName(String materialName) {
        return this.fluidNameTemplate.replace("{material}", materialName);
    }

    public boolean shouldGenerateFor(FluidProperties properties) {
        return switch (this) {
            case FLUID -> true;
            case PLASMA -> properties.shouldGeneratePlasma();
        };
    }

    public boolean shouldGenerateFluidBlock() {
        return generateFluidBlock;
    }
}
