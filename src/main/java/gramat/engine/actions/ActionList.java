package gramat.engine.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ActionList implements Iterable<Action> {

    private final ArrayList<Action> actions;

    public ActionList() {
        actions = new ArrayList<>();
    }

    public void addAll(ActionList items) {
        for (var item : items) {
            this.add(item);
        }
    }

    public boolean add(Action item) {
        if (actions.contains(item)) {
            return false;
        }

        // check if it is already overridden
        for (var action : actions) {
            if (action.overrides.contains(item)) {
                return false;
            }
        }

        // remove overridden actions
        for (var i = actions.size() - 1; i >= 0; i--) {
            if (item.overrides.contains(actions.get(i))) {
                actions.remove(i);
            }
        }

        actions.add(item);
        return true;
    }

    @Override
    public Iterator<Action> iterator() {
        return actions.iterator();
    }

    public void addAll(Action[] items) {
        Collections.addAll(actions, items);
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }
}
