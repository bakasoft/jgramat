package stone.examiners.impl;

import stone.errors.StoneException;
import stone.examiners.ObjectExaminer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class GenericObjectExaminer implements ObjectExaminer {

    private final Map<String, Function<Object, Object>> attributes;
    private final String name;

    public GenericObjectExaminer(Class<?> type, String name) {
        this.attributes = generateAttributes(type);
        this.name = name;
    }

    public static Map<String, Function<Object, Object>> generateAttributes(Class<?> type) {
        var result = new LinkedHashMap<String, Function<Object,Object>>();

        for (var field : type.getFields()) {
            var name = field.getName();

            result.put(name, instance -> {
                try {
                    return field.get(instance);
                }
                catch (IllegalAccessException e) {
                    throw new StoneException(e);
                }
            });
        }

        for (var method : type.getMethods()) {
            var name = parseGetterName(method);
            if (name != null) {
                result.put(name, instance -> {
                    try {
                        return method.invoke(instance);
                    }
                    catch (IllegalAccessException e) {
                        throw new StoneException(e);
                    }
                    catch (InvocationTargetException e) {
                        throw new StoneException(e.getTargetException());
                    }
                });
            }
        }

        return result;
    }

    private static String parseGetterName(Method method) {
        if (method.getParameterCount() == 0) {
            var name = method.getName();

            if (name.startsWith("is")) {
                return name.substring(2, 3).toLowerCase() + name.substring(3);
            }
            else if (name.startsWith("get") && !name.equals("getClass")) {
                return name.substring(3, 4).toLowerCase() + name.substring(4);
            }
        }
        return null;
    }

    @Override
    public Set<String> getKeys(Object value) {
        return attributes.keySet();
    }

    @Override
    public Object getValue(Object value, String key) {
        return attributes.get(key).apply(value);
    }

    @Override
    public String getType() {
        return name;
    }
}
