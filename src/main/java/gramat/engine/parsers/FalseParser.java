package gramat.engine.parsers;

public class FalseParser implements ValueParser {

    @Override
    public Object parse(String value) {
        return false;
    }

    @Override
    public String toString() {
        return "False";
    }
}
