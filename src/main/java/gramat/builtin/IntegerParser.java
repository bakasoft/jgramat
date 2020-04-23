package gramat.builtin;

import gramat.GramatException;
import gramat.compiling.ValueParser;

public class IntegerParser implements ValueParser {

    @Override
    public Object parse(String value) {
        if (value == null) {
            throw new GramatException("Missing integer value.");
        }

        return Integer.parseInt(value);
    }

}
