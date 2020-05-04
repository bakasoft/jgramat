package gramat.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ArrayFilter<T> {

    private ArrayList<T> items;

    public ArrayFilter(T[] items) {
        this.items = new ArrayList<>(List.of(items));
    }

    public boolean empty() {
        return items.isEmpty();
    }

    public boolean singleton() {
        return items.size() == 1;
    }

    public T first() {
        return items.get(0);
    }

    public void map(Function<T, T> mapper) {
        for (int i = 0; i < items.size(); i++) {
            items.set(i, mapper.apply(items.get(i)));
        }
    }

    public <W> void unwrap(Class<W> wrapperType, Function<W, T[]> unwrapper) {
        var result = new ArrayList<T>();

        for (var item : items) {
            if (wrapperType.isInstance(item)) {
                var subItems = unwrapper.apply(wrapperType.cast(item));

                result.addAll(List.of(subItems));
            }
            else {
                result.add(item);
            }
        }

        items = result;
    }

    public <J extends T> void join(Class<J> joinableType, Function<List<J>, T> joiner) {
        var result = new ArrayList<T>();
        var buffer = new ArrayList<J>();

        Runnable flush = () -> {
            if (buffer.size() == 1) {
                result.add(buffer.get(0));
                buffer.clear();
            }
            else if (buffer.size() >= 2) {
                result.add(joiner.apply(buffer));
                buffer.clear();
            }
        };

        for (T item : items) {
            if (joinableType.isInstance(item)) {
                buffer.add(joinableType.cast(item));
            }
            else {
                flush.run();
                result.add(item);
            }
        }

        flush.run();

        items = result;
    }

    public void ignore(Class<?> ignoreType) {
        items.removeIf(ignoreType::isInstance);
    }

    public T[] toArray(IntFunction<T[]> generator) {
        return items.toArray(generator);
    }

}
