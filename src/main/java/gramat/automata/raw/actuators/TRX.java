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

        System.out.println("TRX -------->>>>");

        for  (var state : initial) {
            System.out.println("INI " + state);
        }

        for  (var state : accepted) {
            System.out.println("ACC " + state);
        }

        for (var trn : transitions) {
            System.out.println("TRN " + trn);
        }

        // find machine transitions from initial states
        for (var trn : transitions) {
            if (initial.contains(trn.source)) {
                System.out.println("ADD " + trn + " ! " + start);
                trn.actions.add(start);
            }
        }

        // from the accepted states, find transitions not defined by the machine
        for (var source : accepted) {
            for (var trn : source.getTransitions()) {
                if (!transitions.contains(trn)) {
                    System.out.println("ADD " + trn + " ! " + save);
                    trn.actions.add(save);
                }
            }
        }

        // from the rejected states, find transitions, going to outer states, not defined by the machine
        for (var source : rejected) {
            if (!initial.contains(source)) {
                for (var trn : source.getTransitions()) {
                    if (!transitions.contains(trn) && !total.contains(trn.target)) {
                        System.out.println("ADD " + trn + " ! " + cancel);
                        trn.actions.add(cancel);
                    }
                }
            }
        }

        System.out.println("<<<<-------- TRX");
    }

}
