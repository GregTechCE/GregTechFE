package gregtech.api.util.registry;

public class AlreadyRegisteredKeyException extends Exception {
    public AlreadyRegisteredKeyException(String key) {
        super("Registry already contains record with key: " + key);
    }
}
