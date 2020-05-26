package gramat.parsing;

import gramat.builtin.*;
import gramat.compiling.ValueParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Options {

    private final HashMap<String, ValueParser> valueParsers;

    private final HashMap<String, Class<?>> types;

    public Options() {
        valueParsers = new HashMap<>();
        types = new HashMap<>();
    }

    public static Options createDefault() {
        var options = new Options();
        options.setParser("boolean", new BooleanParser());
        options.setParser("integer", new IntegerParser());
        options.setParser("number", new NumberParser());
        options.setParser("string", new StringParser());
        options.setParser("hex-to-char", new HexToCharParser());
        return options;
    }

    public void setParser(String name, ValueParser parser) {
        valueParsers.put(name, parser);
    }

    public ValueParser getParser(String name) {
        return valueParsers.get(name);
    }

    public void setType(Class<?> type) {
        setType(type.getSimpleName(), type);
    }

    public void setType(String name, Class<?> type) {
        types.put(name, type);
    }

    public Class<?> getType(String name) {
        return types.get(name);
    }


}
