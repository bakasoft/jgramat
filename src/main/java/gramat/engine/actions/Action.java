package gramat.engine.actions;

import java.util.ArrayList;
import java.util.List;

abstract public class Action {

    private static int next_order = 0;

    abstract public String getDescription();

    public final List<Action> overrides;

    private final int order;

    public Action() {
        order = next_order;
        next_order++;
        overrides = new ArrayList<>();
    }

    public void overrides(Action action) {
        overrides.add(action);
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public final int getOrder() {
        return order;
    }
}
