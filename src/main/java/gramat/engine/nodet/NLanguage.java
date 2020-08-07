package gramat.engine.nodet;

import gramat.GramatException;
import gramat.engine.symbols.Symbol;

import java.util.*;

public class NLanguage {

    public final NStateList states;
    public final NTransitionList transitions;

    private int next_state_id = 1;

    public NLanguage() {
        this.states = new NStateList();
        this.transitions = new NTransitionList();
    }

    public NState newState() {
        var id = next_state_id;

        next_state_id++;

        return newState(String.valueOf(id));
    }

    public NState newState(String id) {
        if (states.containsID(id)) {
            throw new GramatException("ID already defined: " + id);
        }

        var state = new NState(this, id);

        states.add(state);

        return state;
    }

    public NTransition newTransition(NState source, NState target, Symbol symbol) {
        var transition = new NTransition(source, target, symbol);

        transitions.add(transition);

        return transition;
    }

    public void newEmptyTransition(NState source, NState target) {
        newTransition(source, target, null);
    }

    public NTransitionList findTransitionsBySource(NState source) {
        var result = new NTransitionList();

        for (var transition : transitions) {
            if (transition.source == source) {
                result.add(transition);
            }
        }

        return result;
    }

    public NTransitionList findTransitionsByTarget(NState target) {
        var result = new NTransitionList();

        for (var transition : transitions) {
            if (transition.target == target) {
                result.add(transition);
            }
        }

        return result;
    }

    public NStateList computeEmptyClosure(NState state) {
        var closure = new NStateList();
        var queue = new LinkedList<NState>();

        queue.add(state);

        do {
            var source = queue.remove();

            if (closure.add(source)) {
                for (var trn : source.getTransitions()) {
                    if (trn.symbol == null) {
                        queue.add(trn.target);
                    }
                }
            }
        } while (queue.size() > 0);

        return closure;
    }

    public NTransitionList findTransitionsFrom(NStateList sources, Symbol symbol) {
        var result = new NTransitionList();
        for (var transition : transitions) {
            if (sources.contains(transition.source) && Objects.equals(transition.symbol, symbol)) {
                result.add(transition);
            }
        }
        return result;
    }

    protected void delete(NTransition transition) {
        transitions.remove(transition);
    }

    public void delete(NState state) {
        transitions.removeIf(t -> t.source == state || t.target == state);
        states.remove(state);
    }
}
