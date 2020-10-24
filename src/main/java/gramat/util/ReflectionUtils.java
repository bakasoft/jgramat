package gramat.util;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class ReflectionUtils {
    public static <T> Supplier<T> emptyConstructor(Class<T> type) {
        return () -> {
            try {
                return type.getConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException();
            }
        };
    }
}
