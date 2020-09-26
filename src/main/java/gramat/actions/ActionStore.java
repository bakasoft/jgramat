package gramat.actions;

import gramat.util.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActionStore extends Store<Action> {

    public boolean add(Action action) {
        for (var item : items) {
            if (item.stacks(action)) {
                return false;
            }
        }

        items.add(action);

        items.sort(Comparator.comparingInt(a -> a.trxID));

        return true;
    }

    public void clear() {
        items.clear();
    }

    public Iterable<Action> reverse() {
        var buffer = new ArrayList<>(items);

        Collections.reverse(buffer);

        return buffer;
    }
}
