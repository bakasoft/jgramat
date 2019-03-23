package org.bakasoft.gramat;

import java.util.HashMap;
import java.util.function.Function;

public class DefaultConversions {

    private static final HashMap<String, Class<?>> defaultTypes;
    private static final HashMap<Class<?>, Function<Object, ?>> defaultParsers;

    private static <T> void addDefault(String name, Class<T> type, Function<Object, T> parser) {
        defaultTypes.put(name, type);
        defaultParsers.put(type, parser);
    }

    static {
        defaultTypes = new HashMap<>();
        defaultParsers = new HashMap<>();

        // primitive classes
        addDefault("int", int.class, raw -> {
            if (raw == null) {
                throw new NullPointerException();
            }
            else if (raw instanceof Number) {
                return ((Number)raw).intValue();
            }

            return Integer.parseInt(raw.toString());
        });

        addDefault("Integer", Integer.class, raw -> {
            if (raw == null) {
                return null;
            }
            else if (raw instanceof Number) {
                return ((Number)raw).intValue();
            }

            return Integer.parseInt(raw.toString());
        });

        addDefault("Boolean", Boolean.class, raw -> {
            if (raw == null) {
                return null;
            }
            else if (raw instanceof Boolean) {
                return (Boolean)raw;
            }

            return Boolean.parseBoolean(raw.toString());
        });

        addDefault("String", String.class, raw -> {
            if (raw == null) {
                return null;
            }
            else if (raw instanceof String) {
                return (String)raw;
            }
            return raw.toString();
        });
    }

    public static Class<?> getDefaultType(String name) {
        return defaultTypes.get(name);
    }

    public static Function<Object, ?> getDefaultParser(Class<?> type) {
       return defaultParsers.get(type);
    }
}
