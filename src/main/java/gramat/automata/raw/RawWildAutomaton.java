package gramat.automata.raw;

import gramat.automata.ndfa.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class RawWildAutomaton extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NAutomaton build(Language lang) {
        return lang.automaton((initialSet, acceptedSet) -> {
            var state = lang.state();

            lang.makeWild(state);

            initialSet.add(state);
            acceptedSet.add(state);

            lang.postBuild(() -> {
                resolve_wild_state(lang, state);
            });
        });
    }

    private void resolve_wild_state(Language language, NState wildState) {
        var queue = new LinkedList<NState>();
        var control = new HashSet<NState>();

        for (var trn : wildState.getTransitions()) {
            queue.add(trn.target);
        }

        language.transition(wildState, wildState, new SymbolWild());

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state) && !language.isWild(state)) {
                var transitions = state.getTransitions();
                var hasWilds = transitions.stream().anyMatch(t -> t.symbol instanceof SymbolWild);
                if (!hasWilds && transitions.size() > 0) {
                    language.transition(state, wildState, null);

                    for (var trn : transitions) {
                        queue.add(trn.target);
                    }
                }
            }
        }

        language.wilds.remove(wildState);
    }

}
