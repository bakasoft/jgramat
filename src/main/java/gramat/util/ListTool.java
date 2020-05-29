package gramat.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ListTool {

    public static class Item<T> {
        public T value;
        public boolean first;
        public boolean last;
        public int index;
    }

    public static Iterable<Item<Character>> iterate(String value) {
        return iterate(value.toCharArray());
    }

    public static Iterable<Item<Character>> iterate(char[] array) {
        var index = new AtomicInteger(0);
        return iterate(new Iterator<>() {
            @Override
            public boolean hasNext() {
                return index.get() < array.length;
            }

            @Override
            public Character next() {
                return array[index.getAndIncrement()];
            }
        });
    }

    public static <T> Iterable<Item<T>> iterate(Iterable<T> source) {
        return iterate(source.iterator());
    }

    public static <T> Iterable<Item<T>> iterate(Iterator<T> iterator) {
        return () -> {
            var item = new Item<T>();
            var index = new AtomicInteger(0);
            return new Iterator<>() {
                @Override
                synchronized public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                synchronized public Item<T> next() {
                    item.value = iterator.next();
                    item.index = index.getAndIncrement();
                    item.first = (item.index == 0);
                    item.last = iterator.hasNext();
                    return item;
                }
            };
        };
    }

    public static <T, U extends T> void collapse(Collection<T> items, Class<U> type, Consumer<T> adder, Consumer<List<U>> joiner) {
        var joins = new ArrayList<U>();

        Runnable flush = () -> {
            if (joins.size() == 1) {
                adder.accept(joins.get(0));
                joins.clear();
            }
            else if (joins.size() >= 2) {
                joiner.accept(joins);
                joins.clear();
            }
        };

        for (T item : items) {
            if (type.isInstance(item)) {
                joins.add(type.cast(item));
            }
            else {
                flush.run();
                adder.accept(item);
            }
        }

        flush.run();
    }

    public static <T> List<T> removeNulls(T item1, T item2) {
        if (item1 != null && item2 != null) {
           return List.of(item1, item2);
        }
        else if (item1 != null) {
            return List.of(item1);
        }
        else if (item2 != null) {
            return List.of(item2);
        }
        return List.of();
    }

}
