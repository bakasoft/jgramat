package gramat.automata.ndfa;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class DWildResolver {

    public static void resolve(NAutomaton automaton) {
        new DWildResolver(automaton).run();
    }

    private final NAutomaton automaton;
    private final Language language;

    private DWildResolver(NAutomaton automaton) {
        this.automaton = automaton;
        this.language = automaton.language;
    }

    private void run() {
        var wildStates = list_wild_states();

        for (var wildState : wildStates) {
            resolve_wild_state(wildState);
        }

        language.wilds.removeAll(wildStates);
    }

    private void resolve_wild_state(NState wildState) {
        var queue = new LinkedList<NState>();
        var control = new HashSet<NState>();

        for (var trn : wildState.getTransitions()) {
            queue.add(trn.target);
        }

        language.transition(wildState, wildState, new SymbolWild());

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var stateNullClosure = DMaker.compute_null_closure(Set.of(state));
                var isAccepted = automaton.isAccepted(stateNullClosure);
                var isWild = language.isWild(state);
                if (!isWild && !isAccepted) {
                    var transitions = state.getTransitions();
                    var hasWilds = transitions.stream().anyMatch(t -> t.symbol instanceof SymbolWild);
                    if (!hasWilds) {
                        language.transition(state, wildState, null);

                        for (var trn : transitions) {
                            queue.add(trn.target);
                        }
                    }
                }
            }
        }
    }

    private Set<NState> list_wild_states() {
        return automaton.getStates().stream()
                .filter(language::isWild)
                .collect(Collectors.toSet());
    }

}
