package gramat.automata.raw.actuators;

import gramat.automata.ndfa.*;
import gramat.eval.Action;
import gramat.util.SetOps;

import java.util.*;

public class TRX {

    public static void setupActions(NMachine machine, Action start, Action save, Action cancel) {
        var initial = new HashSet<>(machine.initial);
        var accepted = new HashSet<>(machine.accepted);
        var states = new HashSet<>(machine.states);
        var rejected = sub(states, accepted);

        for (var i : initial) {
            var nullClosure = Utils.compute_null_closure(Set.of(i));
            for (var trn : findAllNotNullTransitions(nullClosure, states)) {
                machine.language.addActionPattern(i, trn.symbol, trn.target, start);
            }
        }


        for (var a : accepted) {
            var nullClosure = Utils.compute_null_closure(Set.of(a));
            for (var trn : findAllNotNullExitTransitions(nullClosure, states)) {
                machine.language.addActionPattern(a, trn.symbol, trn.target, save);
            }
        }


        for (var r : rejected) {
            var nullClosure = Utils.compute_null_closure(Set.of(r));
            if (!SetOps.intersects(nullClosure, accepted)) {
                for (var trn : findAllNotNullExitTransitions(nullClosure, states)) {
                    machine.language.addActionPattern(r, trn.symbol, trn.target, cancel);
                }
            }
        }
    }

    private static Set<NState> sub(Collection<NState> a, Collection<NState> b) {
        var result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }

    private static List<NTransition> findAllNotNullTransitions(Set<NState> sources, Set<NState> targets) {
        var queue = new LinkedList<>(sources);
        var control = new HashSet<NState>();
        var result = new ArrayList<NTransition>();

        while (queue.size() > 0) {
            var source = queue.remove();

            if (control.add(source)) {
                for (var trn : source.getTransitions()) {
                    if (trn.symbol == null) {
                        queue.add(trn.target);
                    }
                    else {
                        boolean add = false;

                        for (var target : Utils.compute_null_closure(Set.of(trn.target))) {
                            if(targets.contains(target)) {
                                add = true;
                                break;
                            }
                        }

                        if (add) {
                            result.add(trn);
                        }
                    }
                }
            }
        }

        return result;
    }

    private static List<NTransition> findAllNotNullExitTransitions(Set<NState> sources, Set<NState> states) {
        var queue = new LinkedList<>(sources);
        var control = new HashSet<NState>();
        var result = new ArrayList<NTransition>();

        while (queue.size() > 0) {
            var source = queue.remove();

            if (control.add(source)) {
                for (var trn : source.getTransitions()) {
                    if (trn.symbol == null) {
                        queue.add(trn.target);
                    }
                    else {
                        boolean add = true;

                        for (var target : Utils.compute_null_closure(Set.of(trn.target))) {
                            if(states.contains(target)) {
                                add = false;
                                break;
                            }
                        }

                        if (add) {
                            result.add(trn);
                        }
                    }
                }
            }
        }

        return result;
    }

}
