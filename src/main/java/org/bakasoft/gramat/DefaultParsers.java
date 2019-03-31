package org.bakasoft.gramat;

import java.util.HashMap;
import java.util.function.Function;

public class DefaultParsers {

    private static final HashMap<String, Function<String, ?>> defaultParsers;

    private static <T> void addDefault(Class<T> type, Function<String, T> parser) {
        defaultParsers.put(type.getSimpleName(), parser);
    }

    static {
        defaultParsers = new HashMap<>();

        // primitive classes
        addDefault(int.class, raw -> {
            if (raw == null) {
                throw new NullPointerException();
            }

            return Integer.parseInt(raw);
        });

        addDefault(Integer.class, raw -> {
            if (raw == null) {
                return null;
            }

            return Integer.parseInt(raw);
        });

        addDefault(Boolean.class, raw -> {
            if (raw == null) {
                return null;
            }

            return Boolean.parseBoolean(raw);
        });

        addDefault(String.class, raw -> raw);
    }

    public static Function<String, ?> getDefaultParser(String name) {
       return defaultParsers.get(name);
    }
}
