package stone.producers;

import gramat.util.DataUtils;
import stone.errors.StoneException;
import stone.reflect.ReflectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClassObjectProducer implements ObjectProducer {

    private final Class<?> type;
    private final Supplier<Object> maker;
    private final Map<String, BiConsumer<Object, Object>> setters;

    public ClassObjectProducer(Class<?> type) {
        this.type = type;
        this.setters = generateSetters(type);
        this.maker = generateMaker(type);
    }

    private Supplier<Object> generateMaker(Class<?> type) {
        Constructor<?> ctr;

        try {
            ctr = type.getConstructor();
        }
        catch (NoSuchMethodException e) {
            throw new StoneException(e);
        }

        return () -> {
            try {
                return ctr.newInstance();
            }
            catch (InstantiationException e) {
                throw new StoneException(e);
            }
            catch (IllegalAccessException e) {
                throw new StoneException(e);
            }
            catch (InvocationTargetException e) {
                throw new StoneException(e.getTargetException());
            }
        };
    }

    private static Map<String, BiConsumer<Object, Object>> generateSetters(Class<?> type) {
        var setters = new LinkedHashMap<String, BiConsumer<Object, Object>>();

        for (var field : type.getFields()) {
            var name = field.getName();
            var dataType = field.getType();

            setters.put(name, (obj, value) -> {
                try {
                    field.set(obj, ReflectUtils.convertTo(value, dataType));
                }
                catch (IllegalAccessException e) {
                    throw new StoneException(e);
                }
            });
        }

        for (var method : type.getMethods()) {
            var name = method.getName();
            if (name.startsWith("set") && method.getParameterCount() == 1) {
                name = name.substring(3, 4).toLowerCase() + name.substring(4);
                var dataType = method.getParameters()[0].getType();
                setters.put(name, (obj, value) -> {
                    try {
                        method.invoke(obj, ReflectUtils.convertTo(value, dataType));
                    } catch (IllegalAccessException e) {
                        throw new StoneException(e);
                    } catch (InvocationTargetException e) {
                        throw new StoneException(e.getTargetException());
                    }
                });
            }
        }

        return setters;
    }

    @Override
    public Object newInstance(String typeName) {
        return maker.get();
    }

    @Override
    public void set(Object obj, String key, Object value) {
        var setter = setters.get(key);

        if (setter == null) {
            throw new StoneException("not setter for " + key);
        }

        setter.accept(obj, value);
    }
}
