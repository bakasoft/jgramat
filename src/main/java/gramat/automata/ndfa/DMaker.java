package gramat.automata.ndfa;

import java.util.*;

public class DMaker {

    public static NAutomaton transform(NAutomaton automaton) {
        return new DMaker(automaton).run();
    }

    private final Language language;
    private final NState initial;
    private final Set<NState> accepts;

    private final Map<String, NState> hashStates;

    private DMaker(NAutomaton automaton) {
        this.language = automaton.language;
        this.initial = automaton.initial;
        this.accepts = automaton.accepts;
        this.hashStates = new HashMap<>();
    }

    private NAutomaton run() {
        var queue = new LinkedList<Set<NState>>();
        var closures = new HashMap<String, Set<NState>>();
        var newStates = new HashMap<String, NState>();

        var initialClosure = compute_null_closure(Set.of(initial));

        queue.add(initialClosure);

        do {
            var sources = queue.remove();
            var sourcesHash = compute_hash(sources);

            if (!closures.containsKey(sourcesHash)) {
                closures.put(sourcesHash, sources);

                var newSource = state_of(sources);

                newStates.put(sourcesHash, newSource);

                for (var symbol : language.symbols) {
                    var targets = compute_targets(sources, symbol);

                    if (targets.size() > 0) {
                        targets = compute_null_closure(targets);

                        var newTarget = state_of(targets);

                        language.transition(newSource, newTarget, symbol);

                        queue.add(targets);
                    }
                }
            }
        } while(queue.size() > 0);

        var initialHash = compute_hash(initialClosure);

        var newInitial = newStates.get(initialHash);
        var newAccepts = new HashSet<NState>();

        for (var newEntry : newStates.entrySet()) {
            var newState = newEntry.getValue();
            var oldClosure = closures.get(newEntry.getKey());

            boolean accepted = false;

            for (var oldAccept : accepts) {
                if (oldClosure.contains(oldAccept)) {
                    accepted = true;
                    break;
                }
            }

            if (accepted) {
                newAccepts.add(newState);
            }
        }

        return new NAutomaton(language, newInitial, newAccepts);
    }

    private NState state_of(Set<NState> states) {
        var hash = compute_hash(states);
        var state = hashStates.get(hash);
        if (state == null) {
            state = language.state();

            hashStates.put(hash, state);
        }
        return state;
    }

    static Set<NState> compute_null_closure(Set<NState> states) {
        var result = new HashSet<NState>();
        var queue = new LinkedList<>(states);

        do {
            var current = queue.remove();

            if (result.add(current)) {
                var trs = current.getTransitions();

                for (var trn : trs) {
                    if (trn.symbol == null) {
                        queue.add(trn.target);
                    }
                }
            }
        } while (queue.size() > 0);

        return result;
    }

    private Set<NState> compute_targets(Set<NState> states, Symbol symbol) {
        var targets = new HashSet<NState>();

        for (var current : states) {
            var closure = compute_null_closure(Set.of(current));

            for (var state : closure) {
                var trs = state.getTransitions();

                for (var trn : trs) {
                    if (trn.symbol != null && trn.symbol == symbol) {
                        targets.add(trn.target);
                    }
                }
            }
        }

        return targets;
    }

    private static String compute_hash(Set<NState> states) {
        var ids = new int[states.size()];
        var i = 0;

        for (var state : states) {
            ids[i] = state.id;
            i++;
        }

        Arrays.sort(ids);

        var output = new StringBuilder();

        for (i = 0; i < ids.length; i++) {
            if (i > 0) {
                output.append('|');
            }

            output.append(ids[i]);
        }

        return output.toString();
    }
}
