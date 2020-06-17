package gramat.automata.raw.actuators;

import gramat.automata.ndfa.*;
import gramat.automata.ndfa.hooks.MachineHook;
import gramat.eval.Action;
import gramat.util.SetOps;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TRX {

    public static MachineHook setupActions(Action start, Action save, Action cancel) {
        return machine -> {
            // find machine transitions from initial states
            for (var trn : machine.initial.getTransitions()) {
                if (machine.transitions.contains(trn)) {
                    if (machine.states.contains(trn.target)) {
                        System.out.println("ADD " + trn + " ! " + start);
                        trn.actions.add(start);
                    }
                }
            }

            // from the accepted states, find transitions not defined by the machine
            for (var trn : machine.accepted.getTransitions()) {
                if (!machine.transitions.contains(trn)) {
                    System.out.println("ADD " + trn + " ! " + save);
                    trn.actions.add(save);
                }
            }

            // from the rejected states, find transitions, going to outer states, not defined by the machine
            var initialClosure = machine.initial.getNullClosure();
            var acceptedClosure = machine.accepted.getInverseNullClosure();
            for (var source : machine.states) {
                if (!initialClosure.contains(source) && !acceptedClosure.contains(source)) {
                    for (var trn : source.getTransitions()) {
                        if (!machine.transitions.contains(trn) && !machine.states.contains(trn.target)) {
                            System.out.println("ADD " + trn + " ! " + cancel);
                            trn.actions.add(cancel);
                        }
                    }
                }
            }

            System.out.println("--- >> D MACHINE");
            System.out.println(machine.getAmCode());
            System.out.println("D MACHINE << ---");
        };
    }

}
