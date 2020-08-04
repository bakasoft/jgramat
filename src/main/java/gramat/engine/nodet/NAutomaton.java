package gramat.engine.nodet;

import gramat.GramatException;
import gramat.engine.AmCode;
import gramat.engine.symbols.Symbol;
import gramat.engine.deter.DState;
import gramat.engine.control.Check;

import java.util.*;

public class NAutomaton extends NRoot {

    private NState initial;
    private final NStateList accepted;

    public NAutomaton(NRoot root, NMachine machine) {
        this(root, machine.initial, new NStateList(machine.accepted));
    }

    public NAutomaton(NRoot root, NState initial, NStateList accepted) {
        super(root.states.copy(), root.transitions.copy(), root.symbols.copy(), root.checks.copy());
        this.initial = initial;
        this.accepted = accepted;
    }

    public DState compile() {
        return null;
    }

    public void makeDeterministic() {
        var queue = new LinkedList<NStateList>();
        var control = new HashSet<String>();
        var stateMaker = new StateMaker();
        var initialClosure = initial.getEmptyClosure();

        queue.add(initialClosure);

        do {
            var oldSources = queue.remove();

            if (control.add(oldSources.computeID())) {
                for (var symbol : symbols) {
                    if (!symbol.isNull()) {
                        for (var check : checks) {
                            var next = makeClosureDeterministic(stateMaker, oldSources, symbol, check);

                            if (next != null) {
                                queue.add(next);
                            }
                        }
                    }
                }
            }
        } while (queue.size() > 0);

        initial = stateMaker.getState(initialClosure.computeID());

        if (initial == null) {
            throw new GramatException("cannot find initial state");
        }

        // remove old states related transitions
        for (var oldState : states.subtract(stateMaker.getStates())) {
            this.delete(oldState);
        }

        generateClearChecks(initial);
    }

    private NStateList makeClosureDeterministic(StateMaker stateMaker, NStateList oldSources, Symbol symbol, Check check) {
        var oldTransitions = findTransitionsFrom(oldSources, symbol, check);

        if (oldTransitions.size() > 0) {
            // get empty closure of all targets
            var oldTargets = oldTransitions.collectTargets().getEmptyClosure();

            if (oldTargets.size() > 0) {
                var newSource = stateMaker.make(oldSources);
                var newTarget = stateMaker.make(oldTargets);

                newTransition(newSource, newTarget, symbol, check);

                return oldTargets;
            }
        }

        return null;
    }

    private void generateClearChecks(NState initial) {
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(initial);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                var stateTransitions = state.getTransitions();

                for (var target : stateTransitions.collectTargets()) {
                    queue.add(target);
                }

                for (var symbol : stateTransitions.collectSymbols()) {
                    NTransition nullCheckTransition = null;
                    var symbolChecks = new HashSet<Check>();

                    for (var transition : stateTransitions.sublistBySymbol(symbol)) {
                        if (symbolChecks.add(transition.getCheck())) {
                            if (transition.isCheckNull()) {
                                if (nullCheckTransition != null) {
                                    throw new RuntimeException("Too much options for null-check!");
                                }
                                nullCheckTransition = transition;
                            }
                        }
                        else {
                            throw new RuntimeException("Non-deterministic check!");
                        }
                    }
                }
            }
        } while (queue.size() > 0);
    }

    public NState getInitial() {
        return initial;
    }

    public NStateList getAccepted() {
        return accepted;
    }

    private class StateMaker {

        private final HashMap<String, NState> newStates;

        public StateMaker() {
            newStates = new HashMap<>();
        }

        public NState make(NStateList items) {
            if (items.isEmpty()) {
                throw new GramatException("unexpected empty state list");
            }

            var id = items.computeID();
            var state = newStates.get(id);

            if (state != null) {
                return state;
            }

            state = newState(states.getUniqueID(id));

            for (var item : items) {
                state.marks.addAll(item.marks);
            }

            newStates.put(id, state);

            return state;
        }

        public NState getState(String id) {
            return newStates.get(id);
        }

        public Collection<NState> getStates() {
            return newStates.values();
        }
    }

}
