package gramat.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Validations {

    public static <T> boolean isAny(T target, T op1, T op2) {
        return target == op1 || target == op2;
    }

    public static <T> T requireEquals(T op1, T op2) {
        if (op1 != op2) {
            throw new RuntimeException();
        }

        return op1;
    }

    public static void requireEmpty(Collection<?> collection) {
        if (collection != null && collection.size() > 0) {
            throw new RuntimeException();
        }
    }

    public static <T extends Collection<?>> T requireNotEmpty(T collection) {
        if (collection == null || collection.isEmpty()) {
            throw new RuntimeException();
        }
        return collection;
    }

    public static <T> T itemAt(List<? extends T> items, int index) {
        if (index < items.size()) {
            return items.get(index);
        }
        else {
            throw new RuntimeException();
        }
    }

    public static <T> boolean tryCastAndTest(Class<T> type, Object value, Predicate<T> condition) {
        if (type.isInstance(value)) {
            return condition.test(type.cast(value));
        }
        return false;
    }

}
