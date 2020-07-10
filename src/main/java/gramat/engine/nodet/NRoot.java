package gramat.engine.nodet;

import gramat.GramatException;
import gramat.engine.*;

import java.util.*;

public class NRoot {

    public final NStateList states;
    public final NTransitionList transitions;
    public final SymbolSource symbols;
    public final BadgeSource badges;

    private int next_state_id = 1;

    public NRoot() {
        this(new NStateList(), new NTransitionList(), new SymbolSource(), new BadgeSource());
    }

    public NRoot(NStateList states, NTransitionList transitions, SymbolSource symbols, BadgeSource badges) {
        this.states = states;
        this.transitions = transitions;
        this.symbols = symbols;
        this.badges = badges;
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

    public void newTransition(NState source, NState target, Symbol symbol, Badge badge) {
        var transition = new NTransition(source, target, symbol, badge);

        transitions.add(transition);
    }

    public void newEmptyTransition(NState source, NState target) {
        newEmptyTransition(source, target, badges.global);
    }

    public void newEmptyTransition(NState source, NState target, Badge badge) {
        newTransition(source, target, null, badge);
    }

    public void newCharTransition(NState source, NState target, char value) {
        newCharTransition(source, target, value, badges.global);
    }

    public void newCharTransition(NState source, NState target, char value, Badge badge) {
        var symbol = symbols.getChar(value);

        newTransition(source, target, symbol, badge);
    }

    public void newRangeTransition(NState source, NState target, char begin, char end) {
        newRangeTransition(source, target, begin, end, badges.global);
    }

    public void newRangeTransition(NState source, NState target, char begin, char end, Badge badge) {
        var symbol = symbols.getRange(begin, end);

        newTransition(source, target, symbol, badge);
    }

    public void newWildTransition(NState source, NState target) {
        newWildTransition(source, target, badges.global);
    }

    public void newWildTransition(NState source, NState target, Badge badge) {
        var symbol = symbols.getWild();

        newTransition(source, target, symbol, badge);
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

    public NStateList computeEmptyClosure(NState state, Badge badge) {
        var closure = new NStateList();
        var queue = new LinkedList<NState>();

        queue.add(state);

        do {
            var source = queue.remove();

            if (closure.add(source)) {
                for (var trn : source.getTransitions()) {
                    if (trn.symbol == null && (trn.badge == null || trn.badge == badge)) {
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
            if (sources.contains(transition.source)
                    && transition.symbol == symbol) {
                result.add(transition);
            }
        }
        return result;
    }

    protected void delete(NState state) {
        transitions.removeIf(t -> t.source == state || t.target == state);
        states.remove(state);
    }
}
