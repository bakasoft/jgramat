package gramat.engine.deter;

import gramat.engine.Action;
import gramat.engine.Badge;
import gramat.engine.Symbol;
import gramat.engine.SymbolSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBuilder {

    public final SymbolSource symbolSource;

    public final List<DState> states;

    public DBuilder() {
        symbolSource = new SymbolSource();
        states = new ArrayList<>();
    }

    public DState getState(int id) {
        return get_state(id, false);
    }

    private DState get_state(int id, boolean accepted) {
        for (var state : states) {
            if (state.id == id) {
                return state;
            }
        }

        var state = new DState(id, accepted);

        states.add(state);

        return state;
    }

    public void accept(int... ids) {
        for (var id : ids) {
            var state = get_state(id, true);

            if (!state.accepted) {
                throw new RuntimeException("Cannot make state " + id + " accepted.");
            }
        }
    }

    public DTransition transition(int source, int target, char symbol, Action... actions) {
        return transition(getState(source), getState(target), symbolSource.getChar(symbol), null, actions);
    }

    public DTransition transition(int source, int target, char symbol, Badge badge, Action... actions) {
        return transition(getState(source), getState(target), symbolSource.getChar(symbol), badge, actions);
    }

    public DTransition transition(DState source, DState target, Symbol symbol, Badge badge, Action... actions) {
        var transition = new DTransition(target, symbol, badge);

        Collections.addAll(transition.actions, actions);

        source.transitions.add(transition);

        return transition;
    }

}
