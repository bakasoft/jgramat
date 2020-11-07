package stone.binary.input.values;

import stone.binary.input.Value;
import stone.binary.input.ValueType;

import java.util.List;
import java.util.Objects;

public class ListValue implements Value {

    private final List<Value> values;

    public ListValue(List<Value> values) {
        this.values = Objects.requireNonNull(values);
    }

    @Override
    public ValueType getType() {
        return ValueType.LIST;
    }

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public List<Value> asList() {
        return values;
    }
}
