package gramat.tools;

import gramat.engine.nodet.NTransition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

abstract public class Store<T> implements Iterable<T> {

    abstract public boolean canAdd(T item);
    abstract public boolean canRemove(T item);

    private final ArrayList<T> items;

    public Store() {
        items = new ArrayList<>();
    }

    public final int size() {
        return items.size();
    }

    public final boolean add(T item) {
        if (canAdd(item)) {
            return items.add(item);
        }
        return false;
    }

    public final boolean contains(T item) {
        return items.contains(item);
    }

    public final Stream<T> stream() {
        return items.stream();
    }

    public final boolean remove(T item) {
        if (canRemove(item)) {
            return items.remove(item);
        }
        return false;
    }

    public final int removeIf(Predicate<T> condition) {
        int count = 0;

        for (int i = items.size() - 1; i >= 0; i--) {
            var item = items.get(i);

            if (canRemove(item) && condition.test(item)) {
                items.remove(i);
                count++;
            }
        }

        return count;
    }

    @Override
    public final Iterator<T> iterator() {
        return items.iterator();
    }

}
