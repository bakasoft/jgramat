package gramat.engine.parsers;

import gramat.GramatException;

public class NumberParser implements ValueParser {

    @Override
    public Object parse(String value) {
        if (value == null) {
            throw new GramatException("Missing integer value.");
        }

        var d = Double.parseDouble(value);

        if ((int)d == d) {
            return (int)d;
        }
        else if ((long)d == d) {
            return (long)d;
        }
        else if ((float)d == d) {
            return (float)d;
        }
        return d;
    }

}
