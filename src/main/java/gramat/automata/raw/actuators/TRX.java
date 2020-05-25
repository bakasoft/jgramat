package gramat.automata.raw.actuators;

import gramat.automata.ndfa.*;
import gramat.eval.Action;

import java.util.*;

public class TRX {

    public static void setupActions(NMachine machine, Action start, Action save, Action cancel) {
        var initial = NStateSet.of(machine.initial);
        var accepted = NStateSet.of(machine.accepted);
        var rejected = NStateSet.of(machine.rejected);
        var inner = NStateSet.of(machine.states);
        var transitions = Set.of(machine.transitions);

        var total = new NStateSet();
        total.add(initial);
        total.add(inner);
        total.add(accepted);

        // find machine transitions from initial states
        for (var trn : transitions) {
            if (initial.contains(trn.source) && inner.contains(trn.target)) {
                trn.actions.add(start);
            }
        }

        // from the accepted states, find transitions not defined by the machine
        for (var source : accepted) {
            for (var trn : source.getTransitions()) {
                if (!transitions.contains(trn)) {
                    trn.actions.add(save);
                }
            }
        }

        // from the rejected states, find transitions, going to outer states, not defined by the machine
        for (var source : rejected) {
            for (var trn : source.getTransitions()) {
                if (!transitions.contains(trn) && !total.contains(trn.target)) {
                    trn.actions.add(cancel);
                }
            }
        }
    }

}
