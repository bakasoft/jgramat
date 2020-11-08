package gramat.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

public class DataUtils {

    public static <R> List<R> mapInt(byte[] items, IntFunction<R> mapper) {
        var result = new ArrayList<R>();

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

    public static <T> Iterable<T> iterate(T[] items1, T[] items2) {
        return () -> new Iterator<>() {
            int i = 0;
            int j = 0;

            @Override
            public boolean hasNext() {
                return i < items1.length || j < items2.length;
            }

            @Override
            public T next() {
                if (i < items1.length) {
                    var result = items1[i];
                    i++;
                    return result;
                }
                else if (j < items2.length) {
                    var result = items2[j];
                    j++;
                    return result;
                }
                else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
