package gramat.actions;

import gramat.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionList implements Iterable<Action> {

    private final List<Action> actions;

    public ActionList() {
        actions = new ArrayList<>();
    }

    public void runAll(Context context) {
        for (var action : actions) {
            action.run(context);
        }
    }

    public void clear() {
        actions.clear();
    }

    public boolean add(Action newAction) {
        for (var action : actions) {
            if (action.stack(newAction)) {
                return false;
            }
        }

        actions.add(newAction);
        return true;
    }

    public int addAll(ActionList actions) {
        int count = 0;

        for (var action : actions.actions) {
            if (this.add(action)) {
                count++;
            }
        }

        return count;
    }

    @Override
    public Iterator<Action> iterator() {
        return actions.iterator();
    }

    public int size() {
        return actions.size();
    }
}
