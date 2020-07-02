package gramat.expressions.capturing;

import gramat.engine.Action;
import gramat.engine.nodet.NMachine;
import gramat.engine.nodet.NMaker;
import gramat.engine.nodet.NState;

import java.util.HashSet;
import java.util.LinkedList;

public class TRX {

    public static void applyActions(NMaker maker, NState initial, NState accepted, Action begin, Action commit, Action rollback) {
        var group = maker.newGroup(begin, commit, rollback);
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(initial);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                if (state == initial) {
                    state.marks.add(group.initialMark);
                }
                else if (state == accepted) {
                    state.marks.add(group.acceptedMark);
                }
                else {
                    state.marks.add(group.contentMark);
                }

                for (var transition : state.getTransitions()) {
                    queue.add(transition.target);
                }
            }
        } while(queue.size() > 0);
    }

    public static Runnable setupActions(NMachine machine, Action being, Action commit, Action rollback) {
        return () -> {
            // find machine transitions from initial states
            for (var trn : machine.initial.getTransitions()) {
                if (machine.transitions.contains(trn) && machine.states.contains(trn.target)) {
                    trn.actions.add(being);
                }
            }

            // from the accepted states, find transitions not defined by the machine
            for (var trn : machine.accepted.getTransitions()) {
                if (!machine.transitions.contains(trn)) {
                    trn.actions.add(commit);
                }
            }

            // from the rejected states, find transitions, going to outer states, not defined by the machine
            var initialClosure = machine.initial.getEmptyClosure();
            var acceptedClosure = machine.accepted.getInverseEmptyClosure();
            for (var source : machine.states) {
                if (!initialClosure.contains(source) && !acceptedClosure.contains(source)) {
                    for (var trn : source.getTransitions()) {
                        if (!machine.transitions.contains(trn) && !machine.states.contains(trn.target)) {
                            trn.actions.add(rollback);
                        }
                    }
                }
            }
        };
    }

}

