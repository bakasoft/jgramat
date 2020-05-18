package gramat.automata.ndfa;

import gramat.eval.Action;

import java.util.*;

public class DMaker {

    public static DState transform(NAutomaton automaton) {
        return new DMaker(automaton).run();
    }

    private final Language language;
    private final Set<NState> initial;
    private final Set<NState> accepts;

    private final Map<String, DState> hashStates;

    private DMaker(NAutomaton automaton) {
        this.language = automaton.language;
        this.initial = automaton.initial;
        this.accepts = automaton.accepted;
        this.hashStates = new HashMap<>();
    }

    private DState run() {
        var queue = new LinkedList<Set<NState>>();
        var closures = new HashMap<String, Set<NState>>();
        var newStates = new HashMap<String, DState>();

        var initialClosure = compute_null_closure(initial);

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

                        make_transition(newSource, newTarget, symbol);

                        queue.add(targets);
                    }
                }
            }
        } while(queue.size() > 0);

        var initialHash = compute_hash(initialClosure);

        var newInitial = newStates.get(initialHash);

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
                newState.accepted = true;
            }
        }

        return newInitial;
    }

    private void make_transition(DState source, DState target, Symbol symbol) {
        DTransition trn;

        if (symbol instanceof SymbolWild) {
            trn = new DTransitionWild(target);
        }
        else if (symbol instanceof SymbolChar) {
            trn = new DTransitionChar(target, ((SymbolChar)symbol).value);
        }
        else if (symbol instanceof SymbolRange) {
            var sr = (SymbolRange)symbol;
            trn = new DTransitionRange(target, sr.begin, sr.end);
        }
        else {
            throw new RuntimeException();
        }

        source.transitions.add(trn);

        validate_transitions(source.transitions);
    }

    private void validate_transitions(List<DTransition> transitions) {
        for (int i = 0; i < transitions.size(); i++) {
            for (int j = 0; j < transitions.size(); j++) {
                if (i != j) {
                    var iTrn = transitions.get(i);
                    var jTrn = transitions.get(j);
                    if (iTrn.intersects(jTrn)) {
                        throw new RuntimeException("Transition " + iTrn + " intersects " + jTrn + ".");
                    }
                }
            }
        }
    }

    private DState state_of(Set<NState> oldStates) {
        var hash = compute_hash(oldStates);
        var newState = hashStates.get(hash);
        if (newState == null) {
            newState = new DState();

//            for (var oldState : oldStates) {
//                newState.onEnter.addAll(oldState.onEnter);
//                newState.onExit.addAll(oldState.onExit);
//            }

            hashStates.put(hash, newState);
        }
        return newState;
    }

    public static Set<NState> compute_null_closure(Set<NState> states) {
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
