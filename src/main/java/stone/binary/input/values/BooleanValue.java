package stone.binary.input.values;

import stone.binary.input.Value;
import stone.binary.input.ValueType;

public class BooleanValue implements Value {
    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOLEAN;
    }

    @Override
    public boolean asBoolean() {
        return value;
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }

    @Override
    public Object getValue() {
        return value;
    }
}
