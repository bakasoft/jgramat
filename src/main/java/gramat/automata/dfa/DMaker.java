package gramat.automata.dfa;

import gramat.automata.ndfa.*;
import gramat.eval.Action;

import java.util.*;

import static gramat.automata.ndfa.Utils.compute_null_closure;

public class DMaker {

    public static DState transform(NMachine machine) {
        return new DMaker(machine).run();
    }

    private final NLanguage language;
    private final Set<NState> initial;
    private final Set<NState> accepts;

    private final Map<String, DState> hashStates;

    private DMaker(NMachine machine) {
        this.language = machine.language;
        this.initial = new HashSet<>(machine.initial);
        this.accepts = new HashSet<>(machine.accepted);
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

                        make_transition(sources, targets, newSource, newTarget, symbol);

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

    private void make_transition(Set<NState> sources, Set<NState> targets, DState source, DState target, Symbol symbol) {
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

        for (var action : compute_transition_actions(sources, symbol, targets)) {
            if (!trn.actions.contains(action)) {
                trn.actions.add(action);
            }
        }

        validate_transitions(source.transitions);

        source.transitions.add(trn);
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

            hashStates.put(hash, newState);
        }
        return newState;
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

    private List<Action> compute_transition_actions(Set<NState> sources, Symbol symbol, Set<NState> targets) {
        var result = new ArrayList<Action>();

        for (var actionPattern : language.actionPatterns) {
            if (sources.contains(actionPattern.source)
                    && actionPattern.symbol == symbol
                    && targets.contains(actionPattern.target)) {
                result.add(actionPattern.action);
            }
        }

        return result;
    }

    private Set<NTransition> find_all_transitions(Set<NState> sources, Set<NState> targets) {
        var result = new HashSet<NTransition>();
        var queue = new LinkedList<>(sources);
        var control = new HashSet<NState>();

        while (queue.size() > 0) {
            var source = queue.remove();

            if (control.add(source)) {
                for (var trn : source.getTransitions()) {
                    if (targets.contains(trn.target)) {
                        result.add(trn);
                    }

                    if (trn.symbol == null) {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return result;
    }
}
