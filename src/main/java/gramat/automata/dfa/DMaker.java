package gramat.automata.dfa;

import gramat.automata.ndfa.*;

import java.util.*;

public class DMaker {

    public static DState transform(NLanguage language, String name, NStateSet initial, NStateSet accepted, Map<String, DState> optionsMap) {
        return new DMaker(language, name, initial, accepted, optionsMap).run();
    }

    private final NLanguage language;
    private final String name;
    private final NStateSet initial;
    private final NStateSet accepted;
    private final Map<String, DState> optionsMap;

    private final LinkedHashMap<String, DState> hashStates;

    private DMaker(NLanguage language, String name, NStateSet initial, NStateSet accepted, Map<String, DState> optionsMap) {
        this.language = language;
        this.name = name;
        this.initial = initial;
        this.accepted = accepted;
        this.hashStates = new LinkedHashMap<>();
        this.optionsMap = optionsMap;
    }

    private DState run() {
        var queue = new LinkedList<NStateSet>();
        var closures = new LinkedHashMap<String, NStateSet>();
        var newStates = new LinkedHashMap<String, DState>();

        var initialHash = initial.getHash();

        DState newInitial = null;

        queue.add(initial);

        do {
            var sources = queue.remove();
            var sourcesHash = sources.getHash();

            if (!closures.containsKey(sourcesHash)) {
                closures.put(sourcesHash, sources);

                var newSource = state_of(sources);

                if (sourcesHash.equals(initialHash) && newInitial == null) {
                    newInitial = newSource;

                    optionsMap.put(name, newInitial);
                }

                newStates.put(sourcesHash, newSource);

                for (var symbol : language.symbols) {
                    var transitions = findTransitions(sources, symbol);

                    if (transitions.size() > 0) {
                        var targets = new NStateSet();

                        for (var trn : transitions) {
                            targets.add(trn.target);
                        }

                        var newTarget = state_of(targets);

                        make_transition(sources, targets, transitions, newSource, newTarget, symbol);

                        queue.add(targets);
                    }
                }
            }
        } while(queue.size() > 0);

        for (var newEntry : newStates.entrySet()) {
            var newState = newEntry.getValue();
            var oldClosure = closures.get(newEntry.getKey());

            boolean isAccepted = false;

            for (var oldAccept : this.accepted) {
                if (oldClosure.contains(oldAccept)) {
                    isAccepted = true;
                    break;
                }
            }

            if (isAccepted) {
                newState.accepted = true;
            }
        }

        return newInitial;
    }

    private void make_transition(NStateSet sources, NStateSet targets, List<NTransition> transitions, DState source, DState target, Symbol symbol) {
        DTransition dTransition;

        if (symbol.isWild()) {
            dTransition = new DTransitionWild(target);
        }
        else if (symbol.isChar()) {
            dTransition = new DTransitionChar(target, symbol.getChar());
        }
        else if (symbol.isRange()) {
            dTransition = new DTransitionRange(target, symbol.getBegin(), symbol.getEnd());
        }
        else {
            throw new RuntimeException();
        }

        for (var trn : transitions) {
            for (var action : trn.actions) {
                if (!dTransition.actions.contains(action)) {
                    dTransition.actions.add(action);
                }
            }
        }

        validate_transitions(source.transitions);

        source.transitions.add(dTransition);
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

    private DState state_of(NStateSet oldStates) {
        var hash = oldStates.getHash();
        var newState = hashStates.get(hash);
        if (newState == null) {
            newState = new DState();

            hashStates.put(hash, newState);

            // fill options
            for (var state : oldStates) {
                if (state.automata.size() > 0) {
                    for (var automaton : state.automata) {
                        DState amState = optionsMap.get(automaton.name);

                        if (amState == null) {
                            amState = DMaker.transform(
                                    language, automaton.name,
                                    NStateSet.of(automaton.initial), automaton.accepted,
                                    optionsMap);
                            optionsMap.put(automaton.name, amState);
                        }

                        newState.options.add(amState);
                    }
                }
            }
        }
        return newState;
    }

    private List<NTransition> findTransitions(NStateSet states, Symbol symbol) {
        var transitions = new ArrayList<NTransition>();

        for (var state : states) {
            for (var trn : state.getTransitions()) {
                if (trn.symbol == symbol) {
                    transitions.add(trn);
                }
            }
        }

        return transitions;
    }

}
