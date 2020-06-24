package gramat.epsilon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Language {

    public final SymbolSet symbols;

    private final List<State> states;
    private final List<Transition> transitions;
    private int next_state_id;

    public Language() {
        symbols = new SymbolSet();
        states = new ArrayList<>();
        transitions = new ArrayList<>();

        next_state_id = 1;
    }

    public Transition newTransition(State source, State target, Symbol symbol) {
        var transition = new Transition(source, target, symbol);

        source.transitions.add(transition);

        return transition;
    }

    public State newState() {
        var state = new State(this, next_state_id);

        next_state_id++;

        states.add(state);

        return state;
    }

    public List<Transition> findTransitionsByTarget(State target) {
        var result = new ArrayList<Transition>();

        for (var transition : transitions) {
            if (transition.target == target) {
                result.add(transition);
            }
        }

        return result;
    }
}
