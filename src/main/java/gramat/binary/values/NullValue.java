package gramat.binary.values;

import gramat.binary.Value;
import gramat.binary.ValueType;

public class NullValue implements Value {

    public static NullValue INSTANCE = new NullValue();

    private NullValue() {}

    @Override
    public ValueType getType() {
        return ValueType.NULL;
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
