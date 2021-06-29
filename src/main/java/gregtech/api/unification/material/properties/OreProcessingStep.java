package gregtech.api.unification.material.properties;

public enum OreProcessingStep {
    ;

    private final OreProcessingStep fallback;
    private final boolean allowFluid;

    OreProcessingStep(OreProcessingStep fallback, boolean allowFluid) {
        this.fallback = fallback;
        this.allowFluid = allowFluid;
    }

    public OreProcessingStep getFallbackStep() {
        return fallback;
    }

    public boolean isAllowFluid() {
        return allowFluid;
    }
}
