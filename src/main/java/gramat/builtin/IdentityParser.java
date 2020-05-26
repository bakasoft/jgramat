package gramat.builtin;

import gramat.compiling.ValueParser;

public class IdentityParser implements ValueParser {

    private final Object result;

    public IdentityParser(Object result) {
        this.result = result;
    }

    @Override
    public Object parse(String value) {
        return result;
    }
}
