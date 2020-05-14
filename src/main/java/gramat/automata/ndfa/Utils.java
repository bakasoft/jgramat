package gramat.automata.ndfa;

import gramat.automata.actions.Action;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {

    public static void addAction(List<NTransition> transitions, Action action) {
        for (var trn : transitions) {
            trn.actions.add(action);
        }
    }

    public static <T> List<T> removeIf(List<T> items, Predicate<T> condition) {
        return items.stream().filter(i -> !condition.test(i)).collect(Collectors.toUnmodifiableList());
    }

    public static List<NTransition> findExitTransitions(Language lang, Set<NState> sources) {
        var result = new ArrayList<NTransition>();

        for (var trn : lang.transitions) {
            if (sources.contains(trn.source) && !sources.contains(trn.target)) {
                result.add(trn);
            }
        }

        return result;
    }

    public static List<NTransition> findAllTransitionsBySource(Language language, Set<NState> sources) {
        var result = new ArrayList<NTransition>();
        var queue = new LinkedList<>(sources);
        var control = new HashSet<NState>();

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : state.getTransitions()) {
                    result.add(trn);

                    if (trn.symbol == null) {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return result;
    }

    public static List<NTransition> findAllTransitionsByTarget(Language language, Set<NState> targets) {
        var result = new ArrayList<NTransition>();
        var queue = new LinkedList<NState>();
        var nullStates = new HashSet<NState>();

        for (var trn : language.transitions) {
            if (targets.contains(trn.target)) {
                result.add(trn);

                queue.add(trn.source);

                if (trn.symbol == null) {
                    nullStates.add(trn.source);
                }
            }
        }

        // find all null-transitions going to the queue states

        var control = new HashSet<NState>();

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : language.transitions) {
                    if (trn.target == state && !result.contains(trn)) {
                        if (trn.symbol == null) {
                            result.add(trn);

                            queue.add(trn.source);

                            nullStates.add(trn.source);
                        }
                        else if(nullStates.contains(trn.target)) {
                            result.add(trn);
                        }
                    }
                }
            }
        }

        return result;
    }
}
