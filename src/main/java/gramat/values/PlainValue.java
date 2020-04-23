package gramat.values;

public class PlainValue implements StringValue {

    private final String value;
    private final String parser;

    public PlainValue(String value, String parser) {
        this.value = value;
        this.parser = parser;
    }

    @Override
    public Object build() {
        // TODO: implement parser logic
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}