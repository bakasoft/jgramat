package gramat.binary.values;

import gramat.binary.Value;
import gramat.binary.ValueType;

import java.util.List;

public class IntegerValue implements Value {

    private final int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public int asInt() {
        return value;
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }

    @Override
    public List<Value> asList() {
        return List.of(this);
    }
}
