package gregtech.api.block.machine.module.impl.config;

import gregtech.api.block.machine.module.MachineModuleConfig;

public class OrientationConfig implements MachineModuleConfig {

    private final boolean supportsVerticalOrientation;

    private OrientationConfig(boolean supportsVerticalOrientation) {
        this.supportsVerticalOrientation = supportsVerticalOrientation;
    }

    public boolean supportsVerticalOrientation() {
        return supportsVerticalOrientation;
    }

    public static OrientationConfig horizontalOnly() {
        return new OrientationConfig(false);
    }

    public static OrientationConfig horizontalAndVertical() {
        return new OrientationConfig(true);
    }
}
