package org.bakasoft.gramat;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultParsers {

    public static void init(Map<String, Function<String,?>> parsers) {
        parsers.put("int", raw -> {
            if (raw == null) {
                throw new NullPointerException();
            }

            return Integer.parseInt(raw);
        });

        parsers.put("Integer", raw -> {
            if (raw == null) {
                return null;
            }

            return Integer.parseInt(raw);
        });

        parsers.put("Boolean", raw -> {
            if (raw == null) {
                return null;
            }

            return Boolean.parseBoolean(raw);
        });

        parsers.put("String", raw -> raw);
    }

}
