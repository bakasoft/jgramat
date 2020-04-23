package gramat.builtin;

import gramat.GramatException;
import gramat.compiling.ValueParser;

public class BooleanParser implements ValueParser {

    @Override
    public Object parse(String value) {
        if (value == null) {
            return false;
        }

        var lower = value.trim().toLowerCase();

        if ("true".equals(lower) || "1".equals(lower)) {
            return true;
        }
        else if ("false".equals(lower) || "0".equals(lower) || lower.isEmpty()) {
            return false;
        }
        else {
            throw new GramatException("Invalid boolean value: " + value);
        }
    }

}
