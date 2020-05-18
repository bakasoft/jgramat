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

        var fromItoS = findAllNotNullTransitions(initial, notInitial);

        if (start != null) {
            Utils.addAction(fromItoS, start);
        }

        var fromAtoX = findAllNotNullExitTransitions(accepted, states);

        if (save != null) {
            Utils.addAction(fromAtoX, save);
        }

        var fromRtoX = findAllNotNullExitTransitions(rejected, states);

        if (cancel != null) {
            Utils.addAction(fromRtoX, cancel);
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
                    else {
                        if (trn.symbol.toString().equals("\":\"")) {
                            System.out.println("POLLO");
                        }
                        if(!states.contains(trn.target)) {
                            result.add(trn);
                        }
                    }
                }
            }
        }

        return result;
    }

}
