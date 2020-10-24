package gramat.binary;

import gramat.binary.values.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Value {

    static Value of(Object any) {
        if (any == null) {
            return NullValue.INSTANCE;
        }
        else if (any instanceof String) {
            return new StringValue((String)any);
        }
        else if (any instanceof Integer) {
            return new IntegerValue((Integer)any);
        }
        else if (any instanceof Boolean) {
            return new BooleanValue((Boolean)any);
        }
        else if (any instanceof Collection) {
            return new ListValue(list((Collection<?>)any));
        }
        else {
            return new ObjectValue(any);
        }
    }

    static List<Value> list(Collection<?> items) {
        return items.stream().map(Value::of).collect(Collectors.toList());
    }

    ValueType getType();

    Object getValue();

    default String asString() {
        throw new UnsupportedOperationException();
    }

    default int asInt() {
        throw new UnsupportedOperationException();
    }

    default boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    default <T> T asObject(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    default List<Value> asList() {
        throw new UnsupportedOperationException();
    }

    default boolean isNull() {
        return getType() == ValueType.NULL;
    }

    default Integer asIntOrNull() {
        if (isNull()) {
            return null;
        }
        else {
            return asInt();
        }
    }

    default <T> List<T> asListOf(Class<T> itemType) {
        var list = new ArrayList<T>();

        for (var item : asList()) {
            list.add(item.asObject(itemType));
        }

        return list;
    }
}
