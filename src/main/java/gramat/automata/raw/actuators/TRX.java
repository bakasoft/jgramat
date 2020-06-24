package gramat.automata.raw.actuators;

import gramat.epsilon.MachineHook;
import gramat.eval.TRXAction;

public class TRX {

    public static MachineHook setupActions(TRXAction start, TRXAction save, TRXAction cancel) {
        return machine -> {
            // find machine transitions from initial states
            for (var trn : machine.initial.transitions) {
                if (machine.transitions.contains(trn)) {
                    if (machine.states.contains(trn.target)) {
                        System.out.println("ADD " + trn + " ! " + start);
                        trn.actions.add(start);
                    }
                }
            }

            // from the accepted states, find transitions not defined by the machine
            for (var trn : machine.accepted.transitions) {
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
                    for (var trn : source.transitions) {
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
