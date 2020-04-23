package gramat.values;

import gramat.GramatException;
import gramat.compiling.ValueParser;

public class PlainValue implements StringValue {

    private final String value;
    private final ValueParser parser;

    public PlainValue(String value, ValueParser parser) {
        this.value = value;
        this.parser = parser;
    }

    @Override
    public Object build() {
        if (parser == null) {
            return value;
        }
        return parser.parse(value);
    }

    @Override
    public String buildString() {
        if (parser == null) {
            return value;
        }

        var result = build();

        if (result instanceof String) {
            return (String)result;
        }
        else if (result instanceof CharSequence || result instanceof Character) {
            return result.toString();
        }
        else {
            throw new GramatException("Cannot convert to string.");
        }
    }
}
