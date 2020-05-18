package gramat.builtin;

import gramat.compiling.ValueParser;

public class StringParser implements ValueParser {

    @Override
    public Object parse(String value) {
        return value;  // such parsing!
    }

    @Override
    public String toString() {
        return "string";
    }
}
