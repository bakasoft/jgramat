package gramat.automata.dfa;

import gramat.automata.ndfa.*;

import java.util.*;

public class DMaker {

    private final NLanguage language;
    private final String name;
    private final NState initial;
    private final NState accepted;
    private final Map<String, DState> optionsMap;

    private final LinkedHashMap<String, DState> hashStates;

    private final HashMap<NTransition, DTransition> tranmap;

    private final LinkedHashMap<String, NStateSet> closures;
    private final LinkedHashMap<String, DState> newStates;

    public DMaker(NLanguage language, String name, NState initial, NState accepted, Map<String, DState> optionsMap) {
        this.language = language;
        this.name = name;
        this.initial = initial;
        this.accepted = accepted;
        this.hashStates = new LinkedHashMap<>();
        this.optionsMap = optionsMap;
        this.tranmap = new HashMap<>();
        this.closures = new LinkedHashMap<>();
        this.newStates = new LinkedHashMap<>();
    }

    public DState run() {
        var queue = new LinkedList<NStateSet>();
        var initialClosure = initial.getNullClosure();
        var initialHash = initialClosure.getHash();

        DState newInitial = null;

        queue.add(initialClosure);

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
                            for (var target : trn.target.getNullClosure()) {
                                targets.add(target);
                            }
                        }

                        var newTarget = state_of(targets);

                        make_transition(sources, targets, transitions, newSource, newTarget, symbol);

                        queue.add(targets.getNullClosure());
                    }
                }
            }
        } while(queue.size() > 0);

        for (var newEntry : newStates.entrySet()) {
            var newState = newEntry.getValue();
            var oldClosure = closures.get(newEntry.getKey());

            if (oldClosure.contains(accepted)) {
                newState.accepted = true;
            }
        }

        return newInitial;
    }

    private void make_transition(NStateSet sources, NStateSet targets, List<NTransition> nTransitions, DState source, DState target, Symbol symbol) {
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

        for (var nTransition : nTransitions) {
            for (var action : nTransition.actions) {
                if (!dTransition.actions.contains(action)) {
                    dTransition.actions.add(action);
                }
            }

            tranmap.put(nTransition, dTransition);
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

    private DState state_of(NStateSet nStates) {
        var hash = nStates.getHash();
        var dState = hashStates.get(hash);
        if (dState == null) {
            dState = new DState();

            hashStates.put(hash, dState);
        }
        return dState;
    }

    private List<NTransition> findTransitions(NStateSet states, Symbol symbol) {
        var transitions = new ArrayList<NTransition>();
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        for (var state : states) {
            queue.add(state);
        }

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : state.getTransitions()) {
                    if (trn.symbol == symbol) {
                        transitions.add(trn);
                    }
                    else if (trn.symbol == null) {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return transitions;
    }

    public DMachine computeMachine(NMachine nMachine) {
        var dInitial = new HashSet<DState>();
        var dAccepted = new HashSet<DState>();
        var dStates = new HashSet<DState>();
        var dTransitions = new HashSet<DTransition>();

        findDStatesOf(nMachine.initial, dInitial);
        findDStatesOf(nMachine.accepted, dAccepted);

        for (var nState : nMachine.states) {
            findDStatesOf(nState, dStates);
        }

        for (var nTran : nMachine.transitions) {
            var dTran = tranmap.get(nTran);

            if (dTran == null) {
                dTransitions.add(dTran);
            }
        }

        return new DMachine(dInitial, dAccepted, dStates, dTransitions);
    }

    private void findDStatesOf(NState source, Set<DState> result) {
        for (var state : source.getNullClosure()) {
            for (var entry : closures.entrySet()) {
                var hash = entry.getKey();
                var closure =entry.getValue();

                if (closure.contains(state)) {
                    var dState = newStates.get(hash);

                    if (dState == null) {
                        throw new RuntimeException();
                    }

                    result.add(dState);
                }
            }
        }
    }
}
