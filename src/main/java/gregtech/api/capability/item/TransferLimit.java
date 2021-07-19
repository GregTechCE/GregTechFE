package gregtech.api.capability.item;

public enum TransferLimit {
    RESPECT,
    IGNORE;

    public boolean isRespectTransferLimit() {
        return this == RESPECT;
    }

    public boolean isIgnoreTransferLimit() {
        return this == IGNORE;
    }
}
