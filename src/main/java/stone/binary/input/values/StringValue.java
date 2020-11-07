package stone.binary.input.values;

import stone.binary.input.Value;
import stone.binary.input.ValueType;

import java.util.List;
import java.util.Objects;

public class StringValue implements Value {

    private final String value;

    public StringValue(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public int asInt() {
        return Integer.parseInt(value);
    }

    @Override
    public List<Value> asList() {
        return List.of(this);
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public <T> T asObject(Class<T> type) {
        if (type != String.class) {
            throw new ClassCastException();
        }
        return type.cast(value);
    }
}
