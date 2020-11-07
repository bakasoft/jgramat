package gramat.framework;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Configuration {

    private final Map<String, Object> defaults;
    private final Map<String, Object> overrides;

    public Configuration() {
        defaults = new LinkedHashMap<>();
        overrides = new LinkedHashMap<>();
    }

    public void register(ConfigurationEntry entry) {
        if (defaults.containsKey(entry.getKey())) {
            throw new RuntimeException("already defined " + entry.getKey());
        }

        defaults.put(entry.getKey(), entry.getDefault());
    }

    public void registerAll(ConfigurationEntry[] entries) {
        for (var entry : entries) {
            register(entry);
        }
    }

    public Object get(String key) {
        if (overrides.containsKey(key)) {
            return overrides.get(key);
        }
        else if (defaults.containsKey(key)) {
            return defaults.get(key);
        }
        else {
            throw new RuntimeException("unknown key: " + key);
        }
    }

    public Object get(ConfigurationEntry entry) {
        return get(entry.getKey());
    }

    public <T> T get(ConfigurationEntry entry, Class<T> type) {
        var value = get(entry);

        if (value == null) {
            return null;
        }
        else if (type.isInstance(value)) {
            return type.cast(value);
        }
        else {
            throw new RuntimeException("unexpected type: " + value.getClass());
        }
    }

    public boolean getBool(ConfigurationEntry entry) {
        return get(entry, Boolean.class);
    }

}
