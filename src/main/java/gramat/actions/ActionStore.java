package gramat.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionStore implements Iterable<Action> {

    private final List<Action> items;

    public ActionStore() {
        items = new ArrayList<>();
    }

    public ActionStore(ActionStore base) {
        if (base != null) {
            items = new ArrayList<>(base.items);
        }
        else {
            items = new ArrayList<>();
        }
    }

    public boolean contains(Action action) {
        for (var item : items) {
            if (item.contains(action)) {
                return true;
            }
        }
        return false;
    }

    public void append(Action action) {
        if (!contains(action)) {
            items.add(action);
        }
    }

    public void append(ActionStore store) {
        for (var action : store) {
            append(action);
        }
    }

    public void prepend(Action action) {
        if (!contains(action)) {
            items.add(0, action);
        }
    }

    public void prepend(ActionStore store) {
        for (var i = store.items.size() - 1; i >= 0; i--) {
            prepend(store.items.get(i));
        }
    }

    public Action[] toArray() {
        return items.toArray(Action[]::new);
    }

    @Override
    public Iterator<Action> iterator() {
        return items.iterator();
    }

}
