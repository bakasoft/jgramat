package gramat.engine;

import java.util.ArrayList;
import java.util.Collection;

public class ActionList extends ArrayList<Action> {

    public ActionList() {

    }

    public ActionList(Collection<Action> items) {
        super(items);
    }

    public ActionList copy() {
        return new ActionList(this);
    }
}
