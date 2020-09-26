package gramat.util;

import gramat.actions.Action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

abstract public class Store<T> implements Iterable<T> {

    protected final List<T> items;

    public Store() {
        items = new ArrayList<>();
    }

    public T search(Predicate<T> condition) {
        for (var item : items) {
            if (condition.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean contains(Predicate<T> condition) {
        return search(condition) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayList<>(items).iterator();
    }

    public int size() {
        return items.size();
    }
}
