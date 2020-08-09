package gramat.engine.actions;

import java.util.ArrayList;
import java.util.List;

abstract public class Action {

    abstract public String getDescription();

    abstract public int getOrder();

    public final List<Action> overrides;

    public Action() {
        overrides = new ArrayList<>();
    }

    public void overrides(Action action) {
        overrides.add(action);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
