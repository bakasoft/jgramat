package gramat.scheme.common.parsers;

public class StringParser extends NamedParser {

    public StringParser(String name) {
        super(name);
    }

    @Override
    public Object parse(String value) {
        return value;
    }
}
