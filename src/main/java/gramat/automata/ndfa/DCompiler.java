package gramat.automata.ndfa;

import gramat.eval.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DCompiler {

    public static DState transform(NAutomaton automaton) {
        return new DCompiler(automaton).run();
    }

    private final Language language;
    private final NState initial;
    private final Set<NState> accepted;

    private final Map<NState, DState> states;

    private DCompiler(NAutomaton automaton) {
        this.language = automaton.language;
        this.initial = automaton.initial;
        this.accepted = automaton.accepted;
        this.states = new HashMap<>();
    }

    private DState run() {
        if (language.wilds.size() > 0) {
            throw new RuntimeException("Wild states must be resolved before compiling.");
        }

        return get_or_create(initial);
    }

    private DState get_or_create(NState state) {
        DState dstate = states.get(state);

        if (dstate == null) {
            dstate = new DState();

            states.put(state, dstate);

            initialize(dstate, state);
        }

        return dstate;
    }

    private void initialize(DState ds, NState ns) {
        var transitions = new ArrayList<DTransition>();
        var wildTransition = (DTransition) null;

        for (var trn : ns.getTransitions()) {
            if (trn.symbol == null) {
                throw new RuntimeException("Non-deterministic empty transition found: " + trn);
            }

            var actions = trn.actions.toArray(Action[]::new);
            var target = get_or_create(trn.target);

            if (trn.symbol instanceof SymbolWild) {
                if (wildTransition != null) {
                    throw new RuntimeException("Non-deterministic wild transition found: " + trn);
                }

                wildTransition = new DTransitionWild(target, actions);
            }
            else if (trn.symbol instanceof SymbolChar) {
                var sc = (SymbolChar)trn.symbol;
                transitions.add(new DTransitionChar(target, actions, sc.value));
            }
            else if (trn.symbol instanceof SymbolRange) {
                var sr = (SymbolRange)trn.symbol;
                transitions.add(new DTransitionRange(target, actions, sr.begin, sr.end));
            }
            else {
                throw new RuntimeException("Unsupported transition: " + trn);
            }
        }

        ds.accepted = accepted.contains(ns);
        ds.transitions = transitions.toArray(DTransition[]::new);
        ds.wildTransition = wildTransition;
        ds.actions = ns.actions.toArray(Action[]::new);

        validate_transitions(ds.transitions);
    }

    private void validate_transitions(DTransition[] transitions) {
        for (int i = 0; i < transitions.length; i++) {
            for (int j = 0; j < transitions.length; j++) {
                if (i != j) {
                    if (transitions[i].intersects(transitions[j])) {
                        throw new RuntimeException("Transition " + transitions[i] + " intersects " + transitions[j] + ".");
                    }
                }
            }
        }
    }
}
