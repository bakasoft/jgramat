package gramat.util;

import gramat.graph.Node;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Chain<T> implements Iterable<T> {

    public static <T> Chain<T> of(T value) {
        return new Chain<>(new Object[] {value});
    }

    public static <T> Chain<T> of(T value1, T value2) {
        return new Chain<>(new Object[] {value1, value2});
    }

    public static <T> Chain<T> of(List<T> values) {
        return new Chain<>(values.toArray());
    }

    private final Object[] items;

    private Chain(Object[] items) {
        this.items = items;
    }

    /**
     * Assumes that it is a safe operation and returns the casted version of the item.
     * IMPORTANT: THIS METHOD MUST BE USED CAREFULLY!
     */
    @SuppressWarnings("unchecked")
    private T cast(Object item) {
        return (T)item;
    }

    public Chain<T> join(T value) {
        var newItems = new Object[items.length + 1];
        System.arraycopy(items, 0, newItems, 0, items.length);
        newItems[items.length] = value;
        return new Chain<>(newItems);
    }

    public Chain<T> join(List<T> values) {
        var newItems = new Object[items.length + values.size()];
        System.arraycopy(items, 0, newItems, 0, items.length);
        for (var i = 0; i < values.size(); i++) {
            newItems[items.length + i] = values.get(i);
        }
        return new Chain<>(newItems);
    }

    public Chain<T> merge(Chain<T> chain) {
        var newItems = new ArrayList<T>();
        for (var newItem : chain) {
            if (!contains(newItem)) {
                newItems.add(newItem);
            }
        }
        return join(newItems);
    }

    public Chain<T> merge(T value) {
        if (contains(value)) {
            return this;
        }
        return join(value);
    }

    public T get(int index) {
        if (index < 0 || index >= items.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return cast(items[index]);
    }

    public int size() {
        return items.length;
    }

    public boolean contains(T item) {
        for (var thisItem : this.items) {
            if (Objects.equals(thisItem, item)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAny(Chain<? extends T> chain) {
        for (var chainItem : chain.items) {
            for (var thisItem : this.items) {
                if (Objects.equals(thisItem, chainItem)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean anyMatch(Predicate<T> predicate) {
        return Arrays.stream(items).map(this::cast).anyMatch(predicate);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int i = 0;
            @Override
            public boolean hasNext() {
                return i < items.length;
            }
            @Override
            public T next() {
                if (i >= items.length) {
                    throw new NoSuchElementException();
                }
                var value = get(i);
                i++;
                return value;
            }
        };
    }

    public List<T> toList() {
        var list = new ArrayList<T>(items.length);

        iterator().forEachRemaining(list::add);

        return list;
    }

    public <R> List<R> map(Function<T, R> mapper) {
        var result = new ArrayList<R>(items.length);

        iterator().forEachRemaining(item -> result.add(mapper.apply(item)));

        return result;
    }

    @Override
    public String toString() {
        return "Chain[" + StringUtils.join(", ", items) + "]";
    }
}
