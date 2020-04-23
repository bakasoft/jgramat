package gramat.builtin;

import gramat.compiling.ValueParser;

public class HexToCharParser implements ValueParser {

    @Override
    public Object parse(String value) {
        int code = Integer.parseInt(value, 16);

        return (char)code;
    }
}
