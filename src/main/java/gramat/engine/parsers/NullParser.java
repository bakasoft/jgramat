package gramat.engine.parsers;

public class NullParser implements ValueParser {

    @Override
    public Object parse(String value) {
        return null;
    }

    @Override
    public String toString() {
        return "Null";
    }
}
