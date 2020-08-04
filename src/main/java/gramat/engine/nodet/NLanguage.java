package gramat.engine.nodet;

import gramat.GramatException;
import gramat.engine.checks.CheckSource;
import gramat.engine.checks.Check;
import gramat.engine.symbols.Symbol;
import gramat.engine.symbols.SymbolSource;

import java.util.*;

public class NLanguage {

    public final NStateList states;
    public final NTransitionList transitions;
    public final SymbolSource symbols;
    public final CheckSource checks;

    private int next_state_id = 1;

    public NLanguage() {
        this(new NStateList(), new NTransitionList(), new SymbolSource(), new CheckSource());
    }

    public NLanguage(NStateList states, NTransitionList transitions, SymbolSource symbols, CheckSource checks) {
        this.states = states;
        this.transitions = transitions;
        this.symbols = symbols;
        this.checks = checks;
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

    public void newCharTransition(NState source, NState target, char value) {
        var symbol = symbols.getChar(value);

        newTransition(source, target, symbol);
    }

    public void newRangeTransition(NState source, NState target, char begin, char end) {
        var symbol = symbols.getRange(begin, end);

        newTransition(source, target, symbol);
    }

    public void newWildTransition(NState source, NState target) {
        var symbol = symbols.getWild();

        newTransition(source, target, symbol);
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
                    if (trn.symbol == null) {
                        result.add(trn.source);

                        queue.add(trn.source);
                    }
                }
            }
        }
        while(queue.size() > 0);

        return result;
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