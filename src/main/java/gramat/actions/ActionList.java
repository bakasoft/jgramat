package gramat.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionList implements Iterable<Action> {

    public static ActionList empty() {
        return new ActionList(new ArrayList<>());
    }

    public static ActionList copy(ActionList list) {
        return new ActionList(new ArrayList<>(list.items));
    }

    private ArrayList<Action> items;

    private ActionList(ArrayList<Action> items) {
        this.items = items;
    }

    public boolean prepend(Action item) {
        if (items.contains(item)) {
            return false;
        }

        items.add(0, item);
        return true;
    }

    public void prepend(ActionList actions) {
        var newItems = new ArrayList<Action>();

        for (var item : actions.items) {
            if (!newItems.contains(item)) {
                newItems.add(item);
            }
        }

        for (var item : this.items) {
            if (!newItems.contains(item)) {
                newItems.add(item);
            }
        }

        this.items = newItems;
    }

    public boolean append(Action item) {
        if (items.contains(item)) {
            return false;
        }

        items.add(item);
        return true;
    }

    public void append(ActionList actions) {
        for (var item : actions.items) {
            if (!items.contains(item)) {
                items.add(item);
            }
        }
    }

    @Override
    public Iterator<Action> iterator() {
        return items.iterator();
    }

    public Action[] toArray() {
        return items.toArray(Action[]::new);
    }
}
