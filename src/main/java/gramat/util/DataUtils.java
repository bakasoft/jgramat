package gramat.util;

import gramat.models.expressions.ModelExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataUtils {

    public static <T, R> List<R> map(Collection<T> items, Function<T, R> mapper) {
        var result = new ArrayList<R>(items.size());

        for (var item : items) {
            result.add(mapper.apply(item));
        }

        return result;
    }

    public static <T, R> List<R> map(Iterable<T> items, Function<T, R> mapper) {
        var result = new ArrayList<R>();

        for (var item : items) {
            result.add(mapper.apply(item));
        }

        return result;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> List<T> makeImmutableList(T item) {
        if (item == null) {
            return List.of();
        }
        return List.of(item);
    }

    public static <T> List<T> makeImmutableList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return Collections.unmodifiableList(list);
    }

    public static <T> List<T> makeImmutableList(T item1, T item2) {
        if (item1 != null && item2 != null) {
            return List.of(item1, item2);
        }
        else if (item1 != null) {
            return List.of(item1);
        }
        else if (item2 != null) {
            return List.of(item2);
        }
        else {
            return List.of();
        }
    }

    public static <T> void iterate(Iterable<T> items, Consumer<T> content, Runnable separator) {
        if (items != null) {
            var count = 0;

            for (var item : items) {
                if (count > 0) {
                    separator.run();
                }
                content.accept(item);
            }
        }
    }
}
