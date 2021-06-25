package gregtech.api.util.registry;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GTRegistry<T extends GTRegistryKey> {

    protected final Map<String, T> registrar = new HashMap<>();

    public void put(@NotNull T data) throws AlreadyRegisteredKeyException {
        Validate.notNull(data);

        String key = data.getKey();

        Validate.notNull(key);

        if (registrar.containsKey(key)) {
            throw new AlreadyRegisteredKeyException(key);
        }

        registrar.put(key, data);
    }

    public T get(String key) {
        Validate.notNull(key);

        return registrar.get(key);
    }
}
