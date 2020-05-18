package gramat.automata.raw.actuators;

import gramat.automata.ndfa.*;
import gramat.eval.Action;

import java.util.*;

public class TRX {

    public static void setupActions(NAutomaton am, Action start, Action save, Action cancel) {
        var initial = am.initial;
        var accepted = am.accepted;
        var states = am.states;
        var rejected = sub(states, accepted);

        for (var trn : findAllNotNullTransitions(initial, states)) {
            am.language.addActionPattern(initial, trn.symbol, trn.target, start);
        }

        for (var trn : findAllNotNullExitTransitions(accepted, states)) {
            am.language.addActionPattern(accepted, trn.symbol, trn.target, save);
        }

        for (var trn : findAllNotNullExitTransitions(rejected, states)) {
            for (var r : rejected) {
                var actuallyRejected = true;

                for (var s : Utils.compute_null_closure(Set.of(r))) {
                    if (accepted.contains(s)) {
                        actuallyRejected = false;
                        break;
                    }
                }

                if (actuallyRejected) {
                    am.language.addActionPattern(r, trn.symbol, trn.target, cancel);
                }
            }
        }
    }

    private static Set<NState> sub(Set<NState> a, Set<NState> b) {
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
