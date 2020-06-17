package gramat.automata.dfa;

import gramat.automata.ndfa.*;
import gramat.eval.Action;

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

        apply_actions();

        return newInitial;
    }

    private void apply_actions() {
        for (var nTransition : language.transitions) {
            if (nTransition.actions.size() > 0) {
                var sourceClosure = nTransition.source.getInverseNullClosure();
                var dSources = findContainingStates(sourceClosure);
                for (var trn : findNotNullTransitions(nTransition)) {
                    for (var dSource : dSources) {
                        System.out.println("~~~~~~~~~~~~~~~");
                        for (var a : nTransition.actions) { System.out.println("ACTION " + a); }
                        System.out.println("SOURCE " + dSource.id);
                        for (var dTransition : dSource.transitions) {
                            System.out.println("SYMBOL " + dTransition.getSymbol());
                            if (trn.symbol == null || transition_matches(dTransition, trn.symbol)) {
                                var targetClosure = trn.target.getNullClosure();
                                var dTargets = findContainingStates(targetClosure);
                                for (var dTarget : dTargets) {
                                    System.out.println("TARGET " + dTarget.id);
                                    if (dTransition.target == dTarget) {
                                        dTransition.addActions(nTransition.actions);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private List<NTransition> findNotNullTransitions(NTransition transition) {
        if (transition.symbol != null) {
            return List.of(transition);
        }

        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();
        var result = new ArrayList<NTransition>();

        queue.add(transition.source);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : state.getTransitions()) {
                    if (trn.symbol != null) {
                        result.add(trn);
                    }
                    else {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return result;
    }

    private boolean transition_matches(DTransition dTransition, Symbol symbol) {
        if (dTransition instanceof DTransitionChar) {
            return ((DTransitionChar)dTransition).symbol == symbol.getChar();
        }
        else if (dTransition instanceof DTransitionRange) {
            var dtr = (DTransitionRange)dTransition;
            return dtr.begin == symbol.getBegin() && dtr.end == symbol.getEnd();
        }
        else if (dTransition instanceof DTransitionWild) {
            return symbol.isWild();
        }
        return false;
    }

    private Set<DState> findIntersectedStates(NStateSet states) {
        var result = new HashSet<DState>();

        for(var entry : closures.entrySet()) {
            var hash = entry.getKey();
            var closure = entry.getValue();

            if (closure.containsAny(states)) {
                var dstate = newStates.get(hash);

                result.add(Objects.requireNonNull(dstate));
            }
        }

        return result;
    }

    private Set<DState> findContainingStates(NStateSet states) {
        var result = new HashSet<DState>();

        for(var entry : closures.entrySet()) {
            var hash = entry.getKey();
            var closure = entry.getValue();

            if (closure.containsAll(states)) {
                var dstate = newStates.get(hash);

                result.add(Objects.requireNonNull(dstate));
            }
        }

        return result;
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
            dState = new DState(hash);

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
