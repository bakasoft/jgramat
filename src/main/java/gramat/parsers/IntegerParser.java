package gramat.parsers;

public class IntegerParser extends NamedParser {

    public IntegerParser(String name) {
        super(name);
    }

    @Override
    public Object parse(String value) {
        if (value == null) {
            throw new RuntimeException("Missing integer value.");
        }

        return Integer.parseInt(value);
    }

}