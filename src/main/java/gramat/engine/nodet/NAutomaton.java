package gramat.engine.nodet;

import gramat.GramatException;
import gramat.engine.AmCode;
import gramat.engine.deter.DState;

import java.util.*;
import java.util.function.Function;

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
        move_badges_to_non_null();

        var queue = new LinkedList<NStateList>();
        var control = new HashSet<String>();
        var newStates = new HashMap<String, NState>();
        var stateMaker = (Function<NStateList, NState>)((NStateList items) -> {
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
        });

        var initialClosure = initial.getEmptyClosure();

        queue.add(initialClosure);

        do {
            var oldSources = queue.remove();

            if (control.add(oldSources.computeID())) {
                for (var symbol : symbols) {
                    var oldTransitions = findTransitionsFrom(oldSources, symbol);

                    if (oldTransitions.size() > 0) {
                        // get empty closure of all targets
                        var oldTargets = oldTransitions.collectTargets().getEmptyClosure();

                        if (oldTargets.size() > 0) {
                            var newSource = stateMaker.apply(oldSources);
                            var newTarget = stateMaker.apply(oldTargets);

                            newTransition(newSource, newTarget, symbol, null);

                            queue.add(oldTargets);
                        }
                    }
                }
            }
        } while (queue.size() > 0);

        initial = newStates.get(initialClosure.computeID());

        if (initial == null) {
            throw new GramatException("cannot find initial state");
        }

        // remove old states related transitions
        for (var oldState : states.subtract(newStates.values())) {
            this.delete(oldState);
        }

        System.out.println("NDFA >>>>>>>>>>");
        AmCode.write(System.out, initial, accepted);
        System.out.println("<<<<<<<<<< NDFA");
    }

    private void move_badges_to_non_null() {
//        while(true) {
//            NTransition currTransition = null;
//
//            for (var transition : transitions) {
//                if (transition.symbol == null && transition.badge != null) {
//                    currTransition = transition;
//                    break;
//                }
//            }
//
//            if (currTransition == null) {
//                break;
//            }
//
//            var nextTransitions = currTransition.target.getTransitions();
//
//            for (var nextTransition : nextTransitions) {
//                if (nextTransition.badge == null) {
//                    nextTransition.badge = currTransition.badge;
//                } else {
//                    newTransition(
//                            nextTransition.source,
//                            nextTransition.target,
//                            nextTransition.symbol,
//                            currTransition.badge);
//                }
//            }
//
//            currTransition.badge = null;
//        }
//
//
//        System.out.println("NDFA >>>>>>>>>>");
//        AmCode.write(System.out, initial, accepted);
//        System.out.println("<<<<<<<<<< NDFA");
    }

}
