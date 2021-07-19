package gregtech.api.capability.item;

public enum DischargeMode {
    INTERNAL,
    EXTERNAL;

    public boolean isExternal() {
        return this == EXTERNAL;
    }

    public boolean isInternal() {
        return this == INTERNAL;
    }
}
