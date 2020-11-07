package gramat.util;

import java.util.*;

public class Args {

    public static Args empty() {
        return new Args();
    }

    public static Args of(Map<?, ?> map) {
        var args = new Args();
        for (var entry : map.entrySet()) {
            var name = String.valueOf(entry.getKey());
            var value = entry.getValue();

            args.set(name, value);
        }
        return args;
    }

    public static Args of(Collection<?> values, Collection<String> keys) {
        var args = new Args();
        var vIter = values.iterator();
        var kIter = keys.iterator();

        while (vIter.hasNext()) {
            if (kIter.hasNext()) {
                var value = vIter.next();
                var key = kIter.next();

                args.set(key, value);
            }
            else {
                throw new RuntimeException();
            }
        }

        return args;
    }

    private static final Object MANDATORY = new Object();

    private final Map<String, Object> map;

    private final Set<String> usedKeys;

    private Args() {
        map = new LinkedHashMap<>();
        usedKeys = new HashSet<>();
    }

    public Object get(String key, Object defaultValue) {
        var result = map.getOrDefault(key, defaultValue);

        usedKeys.add(key);

        if (result == MANDATORY) {
            throw new RuntimeException("argument not found: " + key);
        }

        return result;
    }

    public Object get(String key) {
        return get(key, MANDATORY);
    }

    public String getString(String key) {
        return toString(get(key));
    }

    public String getString(String key, String defaultValue) {
        return toString(get(key, defaultValue));
    }

    public int getInt(String key) {
        return toInt(get(key));
    }

    public int getInt(String key, int defaultValue) {
        return toInt(get(key, defaultValue));
    }

    public void set(String key, Object value) {
        if (map.get(key) != null) {
            throw new RuntimeException();
        }
        map.put(key, value);
        usedKeys.add(key);
    }

    public Set<String> getUnknownKeys() {
        var result = new HashSet<>(map.keySet());
        result.removeAll(usedKeys);
        return result;
    }

    public void validateUnknownKeys() {
        var notUsedKeys = getUnknownKeys();

        if (!notUsedKeys.isEmpty()) {
            throw new RuntimeException("Unknown keys: " + notUsedKeys);
        }
    }

    private static int toInt(Object value) {
        if (value == null) {
            throw new RuntimeException();
        }
        else if (value instanceof Integer) {
            return (Integer) value;
        }
        else if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        else if (value instanceof String) {
            return Integer.parseInt((String)value);
        }
        else {
            throw new RuntimeException();
        }
    }

    private static String toString(Object value) {
        if (value == null) {
            return null;
        }

        return value.toString();
    }

}
