package gramat.runtime;

import gramat.expressions.Expression;

import java.util.HashMap;
import java.util.HashSet;

public class Circuit {

    private final HashMap<Expression, HashSet<Integer>> state;

    public Circuit() {
        this.state = new HashMap<>();
    }

    public boolean enter(Expression expression, int position) {
        var positions = state.get(expression);

        if (positions != null) {
            return positions.add(position);
        }

        positions = new HashSet<>();
        state.put(expression, positions);
        return positions.add(position);
    }

    public void remove(Expression expression, int position) {
        var positions = state.get(expression);

        if (positions != null) {
            positions.remove(position);

            if (positions.isEmpty()) {
                state.remove(expression);
            }
        }
    }

}
