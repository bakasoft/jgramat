package gramat.automata.raw.actuators;

import gramat.automata.ndfa.*;
import gramat.eval.Action;
import gramat.util.SetOps;

public class TRX {

    public static NContext.MachineHook setupActions(Action start, Action save, Action cancel) {
        return machine -> {
            // find machine transitions from initial states
            for (var state : machine.initial) {
                for (var trn : state.transitions) {
                    if (machine.states.contains(trn.target)) {
                        System.out.println("ADD " + trn + " ! " + start);
                        trn.actions.add(start);
                    }
                }
            }

            // from the accepted states, find transitions not defined by the machine
            for (var source : machine.accepted) {
                for (var trn : source.transitions) {
                    if (!machine.states.contains(trn.target)) {
                        System.out.println("ADD " + trn + " ! " + save);
                        trn.actions.add(save);
                    }
                }
            }

            // from the rejected states, find transitions, going to outer states, not defined by the machine
            for (var source : machine.states) {
                if (!machine.initial.contains(source) && !machine.accepted.contains(source)) {
                    for (var trn : source.transitions) {
                        if (!machine.states.contains(trn.target) && !machine.states.contains(trn.target)) {
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
