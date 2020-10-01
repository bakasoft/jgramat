package gramat.actions;

import gramat.eval.Context;

import java.util.List;

abstract public class Action {

    abstract public boolean contains(Action other);

    abstract public void run(Context context);

    abstract public List<String> getArguments();

    public final String getKey() {
        for (var item : ActionKeys.values()) {
            if (item.type.isInstance(this)) {
                return item.key;
            }
        }

        throw new RuntimeException();
    }

    @Override
    public final String toString() {
        return getKey() + "(" + String.join(", ", getArguments()) + ")";
    }

}
