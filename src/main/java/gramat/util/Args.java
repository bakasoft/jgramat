package gramat.util;

import java.util.*;

public class Args {

    public static Args of(Object any) {
        if (any instanceof Collection) {
            return of((Collection<?>)any);
        }
        else if (any instanceof Map) {
            return of((Map<?,?>)any);
        }
        else {
            var args = new Args();
            args.args.add(new Arg(null, any));
            return args;
        }
    }

    public static Args of(Collection<?> items) {
        var args = new Args();

        for (var item : items) {
            args.args.add(new Arg(null, item));
        }

        return args;
    }

    public static Args of(Map<?, ?> map) {
        var args = new Args();
        for (var entry : map.entrySet()) {
            var name = String.valueOf(entry.getKey());
            var value = entry.getValue();

            args.args.add(new Arg(name, value));
        }
        return args;
    }

    private final Object MANDATORY = new Object();

    private final List<Arg> args;

    private int currentIndex;

    private Args() {
        args = new ArrayList<>();
        currentIndex = 0;
    }

    public <T> T pullAs(T defaultValue, Class<T> type) {
        return convert_value(pull(defaultValue), type);
    }

    public <T> T pullAs(Class<T> type) {
        return convert_value(pull(MANDATORY), type);
    }

    public Object pull(Object defaultValue) {
        return pull(currentIndex, defaultValue);
    }

    private Object pull(int index, Object defaultValue) {
        if (index >= 0 && index < args.size()) {
            currentIndex = index + 1;

            return args.get(index).value;
        }

        if (defaultValue == MANDATORY) {
            throw new RuntimeException("argument not found: at " + index);
        }

        return defaultValue;
    }

    public Object pull(String name, Object defaultValue) {
        // TODO change index accordingly

        for (var arg : args) {
            if (Objects.equals(arg.name, name)) {
                return arg.value;
            }
        }

        if (defaultValue == MANDATORY) {
            throw new RuntimeException("argument not found: " + name);
        }

        return defaultValue;
    }

    private static <T> T convert_value(Object value, Class<T> type) {
        if (value == null && !type.isPrimitive()) {
            return null;
        }
        else if (type.isInstance(value)) {
            return type.cast(value);
        }
        else {
            throw new RuntimeException("invalid type");
        }
    }

    private static class Arg {
        public final String name;
        public final Object value;
        private Arg(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }

}
