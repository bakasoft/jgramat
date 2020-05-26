package gramat.builtin;

import gramat.compiling.ValueParser;

public class TrueParser implements ValueParser {

    @Override
    public Object parse(String value) {
        return true;
    }

    @Override
    public String toString() {
        return "True";
    }
}
