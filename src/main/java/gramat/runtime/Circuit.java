package gramat.runtime;

import gramat.expressions.Expression;

import java.util.HashMap;
import java.util.HashSet;

public class Circuit {

    private final Circuit parent;
    private final HashMap<Expression, HashSet<Integer>> state;

    public Circuit(Circuit parent) {
        this.parent = parent;
        this.state = new HashMap<>();
    }

    private boolean contains(Expression expression, int position) {
        if (parent != null && parent.contains(expression, position)) {
            return true;
        }

        var positions = state.get(expression);

        return positions != null && positions.contains(position);
    }

    public boolean enter(Expression expression, int position) {
        if (contains(expression, position)) {
            return false;
        }

        return state.computeIfAbsent(expression, k -> new HashSet<>()).add(position);
    }

    public Circuit getParent() {
        return parent;
    }

}
