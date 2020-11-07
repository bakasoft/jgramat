package stone.binary.input.values;

import stone.binary.input.Value;
import stone.binary.input.ValueType;

import java.util.List;

public class NullValue implements Value {

    public static NullValue INSTANCE = new NullValue();

    private NullValue() {}

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    @Override
    public String asString() {
        throw new NullPointerException();
    }

    @Override
    public Integer asIntOrNull() {
        return null;
    }

    @Override
    public <T> T asObject(Class<T> type) {
        throw new NullPointerException();
    }

    @Override
    public boolean asBoolean() {
        throw new NullPointerException();
    }

    @Override
    public int asInt() {
        throw new NullPointerException();
    }

    @Override
    public <T> List<T> asListOf(Class<T> itemType) {
        return List.of();
    }

    @Override
    public List<Value> asList() {
        return List.of();
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean isNull() {
        return true;
    }

}
