package gramat.values;

import gramat.GramatException;
import gramat.util.ReflectionTool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class TypedObject extends ObjectValue {

    private final Class<?> type;

    public TypedObject(Class<?> type) {
        this.type = type;
    }

    @Override
    public Object build() {
        Object instance = ReflectionTool.newInstance(type);

        var values = getAttributes();

        values.forEach((key, value) -> {
            ReflectionTool.set(type, instance, key, value.build());
        });

        return instance;
    }
}
