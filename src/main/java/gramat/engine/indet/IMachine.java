package gramat.engine.indet;

import gramat.GramatException;
import gramat.engine.AmCode;
import gramat.engine.nodet.*;
import gramat.engine.symbols.Symbol;
import gramat.engine.deter.DState;
import gramat.engine.control.Check;

import java.util.*;

public class IMachine {

    private NState initial;
    private final NLanguage lang;
    private final NStateList accepted;

    public IMachine(NLanguage lang, NMachine machine) {
        this.lang = lang;
        this.initial = machine.initial;
        this.accepted = new NStateList(machine.accepted);
    }

    public DState compile() {
        makeDeterministic();
        System.out.println("DFA >>>>>>>>>>");
        AmCode.write(System.out, initial, accepted);
        System.out.println("<<<<<<<<<< DFA");
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
                for (var symbol : lang.symbols) {
                    if (!symbol.isNull()) {
                        for (var check : lang.checks) {
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
        for (var oldState : lang.states.subtract(stateMaker.getStates())) {
            lang.delete(oldState);
        }

        generateClearChecks(initial);
    }

    private NStateList makeClosureDeterministic(StateMaker stateMaker, NStateList oldSources, Symbol symbol, Check check) {
        var oldTransitions = lang.findTransitionsFrom(oldSources, symbol, check);

        if (oldTransitions.size() > 0) {
            // get empty closure of all targets
            var oldTargets = oldTransitions.collectTargets().getEmptyClosure();

            if (oldTargets.size() > 0) {
                var newSource = stateMaker.make(oldSources);
                var newTarget = stateMaker.make(oldTargets);

                lang.newTransition(newSource, newTarget, symbol, check);

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

            state = lang.newState(lang.states.getUniqueID(id));

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
