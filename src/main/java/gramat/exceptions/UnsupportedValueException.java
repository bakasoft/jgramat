package gramat.exceptions;

import gramat.util.PP;

public class UnsupportedValueException extends GramatException {

    public UnsupportedValueException(Object value) {
        super(generateMessage(null, value));
    }

    public UnsupportedValueException(Object value, String name) {
        super(generateMessage(name, value));
    }

    private static String generateMessage(String name, Object value) {
        if (value == null) {
            var nameStr = name != null ? name : "value";
            return String.format("Not supported null %s(s).", nameStr);
        }

        var type = value.getClass();
        var nameStr = name != null ? name : type.getSimpleName();
        var valueStrDefault = value.toString();

        if (valueStrDefault.startsWith(type.getName() + "@")) {
            return String.format("Not supported %s: %s", nameStr, valueStrDefault);
        }

        var valueStrPretty = PP.str(valueStrDefault);
        return String.format("Not supported %s: %s", nameStr, valueStrPretty);
    }

}
