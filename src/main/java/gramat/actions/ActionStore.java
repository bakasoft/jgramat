package gramat.actions;

import gramat.util.Store;

public class ActionStore extends Store<Action> {

    public boolean contains(Action action) {
        for (var item : items) {
            if (item.contains(action)) {
                return true;
            }
        }
        return false;
    }

    public boolean add(Action action) {
        if (contains(action)) {
            return false;
        }

        return items.add(action);
    }

    public boolean addTop(Action action) {
        if (contains(action)) {
            return false;
        }

        items.add(0, action);
        return true;
    }

    public void add(ActionStore store) {
        for (var action : store) {
            add(action);
        }
    }
}
