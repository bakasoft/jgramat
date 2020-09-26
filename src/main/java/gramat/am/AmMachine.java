package gramat.am;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AmMachine {

    public final List<AmState> states;
    public final List<AmTransition> transitions;
    public final List<AmTest> tests;

    public AmMachine() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        tests = new ArrayList<>();
    }

    public List<AmTransition> findTransitionsFrom(AmState source) {
        var result = new ArrayList<AmTransition>();

        for (var transition : transitions) {
            if (transition.source == source) {
                result.add(transition);
            }
        }

        return result;
    }

    public AmState findInitialState() {
        var result = new ArrayList<AmState>();

        for (var state : states) {
            if (state.initial != null && state.initial) {
                result.add(state);
            }
        }

        if (result.isEmpty()) {
            return null;
        }
        else if (result.size() != 1) {
            throw new RuntimeException("Too many initial states");
        }

        return result.get(0);
    }

    public AmState searchState(String id) {
        var result = new ArrayList<AmState>();

        for (var state : states) {
            if (Objects.equals(state.id, id)) {
                result.add(state);
            }
        }

        if (result.isEmpty()) {
            return null;
        }
        else if (result.size() != 1) {
            throw new RuntimeException("Ambiguous state: " + id);
        }

        return result.get(0);
    }

}
