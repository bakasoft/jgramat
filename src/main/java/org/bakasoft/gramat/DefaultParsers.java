package org.bakasoft.gramat;

import org.bakasoft.gramat.plugins.ParserPlugin;
import org.bakasoft.gramat.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultParsers {

    public static void init(Map<String, Plugin> parsers) {
        parsers.put("int", new ParserPlugin(raw -> {
            if (raw == null) {
                throw new NullPointerException();
            }

            return Integer.parseInt(raw);
        }));

        parsers.put("Integer", new ParserPlugin(raw -> {
            if (raw == null) {
                return null;
            }

            return Integer.parseInt(raw);
        }));

        parsers.put("Boolean", new ParserPlugin(raw -> {
            if (raw == null) {
                return null;
            }

            return Boolean.parseBoolean(raw);
        }));

        parsers.put("String", new ParserPlugin(raw -> raw));
    }

}
