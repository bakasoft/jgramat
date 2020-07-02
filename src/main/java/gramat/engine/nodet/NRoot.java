package gramat.engine.nodet;

import gramat.engine.Badge;
import gramat.engine.Symbol;
import gramat.engine.SymbolEmpty;
import gramat.engine.SymbolSource;

import java.util.*;

public final class NRoot {

    public final NStateList states;
    public final NTransitionList transitions;
    public final SymbolSource symbols;

    private int next_state_id = 1;

    public NRoot() {
        this.states = new NStateList();
        this.transitions = new NTransitionList();
        this.symbols = new SymbolSource();
    }

    public NState newState() {
        var id = next_state_id;

        next_state_id++;

        var state = new NState(this, id);

        states.add(state);

        return state;
    }

    public NTransition newTransition(NState source, NState target, Symbol symbol, Badge badge) {
        var transition = new NTransition(source, target, symbol, badge);

        transitions.add(transition);

        return transition;
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
                    if (trn.symbol instanceof SymbolEmpty) {
                        queue.add(trn.target);
                    }
                }
            }
        } while (queue.size() > 0);

        return closure;
    }

    public NStateList computeInverseEmptyClosure(NState state) {
        var result = new NStateList();
        var queue = new LinkedList<NState>();
        var control = new NStateList();

        queue.add(state);

        result.add(state);

        do {
            var target = queue.remove();

            if (control.add(target)) {
                for (var trn : findTransitionsByTarget(target)) {
                    if (trn.symbol instanceof SymbolEmpty) {
                        result.add(trn.source);

                        queue.add(trn.source);
                    }
                }
            }
        }
        while(queue.size() > 0);

        return result;
    }
}
