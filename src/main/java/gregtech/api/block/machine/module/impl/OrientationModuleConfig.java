package gregtech.api.block.machine.module.impl;

import gregtech.api.block.machine.module.MachineModuleConfig;

public class OrientationModuleConfig implements MachineModuleConfig {

    private final boolean supportsVerticalOrientation;

    private OrientationModuleConfig(boolean supportsVerticalOrientation) {
        this.supportsVerticalOrientation = supportsVerticalOrientation;
    }

    public boolean supportsVerticalOrientation() {
        return supportsVerticalOrientation;
    }

    public static OrientationModuleConfig horizontalOnly() {
        return new OrientationModuleConfig(false);
    }

    public static OrientationModuleConfig horizontalAndVertical() {
        return new OrientationModuleConfig(true);
    }
}
