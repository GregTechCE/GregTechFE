package gregtech.api.module.impl;

import gregtech.api.module.api.OrientationKind;

public enum StandardOrientationKind implements OrientationKind {
    FRONT_FACING("front_facing"),
    AUTO_OUTPUT_SIDE("auto_output_side"),
    OUTPUT_SIDE("output_side"),
    EXHAUST_SIDE("exhaust_side");

    private final String name;

    StandardOrientationKind(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}
