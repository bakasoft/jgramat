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
        var notInitial = sub(states, initial);

        for (var state : initial) {
            state.onExit.add(start);
        }

        for (var state : accepted) {
            state.onExit.add(save);
        }

//        for (var state : rejected) {
//            state.onExit.add(cancel);
//        }
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
                    else if(targets.contains(trn.target)) {
                        result.add(trn);
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
                    else if(!states.contains(trn.target)) {
                        result.add(trn);
                    }
                }
            }
        }

        return result;
    }

}
