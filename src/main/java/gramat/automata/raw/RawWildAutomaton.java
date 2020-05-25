package gramat.automata.raw;

import gramat.automata.ndfa.*;

import java.util.HashSet;
import java.util.LinkedList;

public class RawWildAutomaton extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        context.language.transitionWild(initial, initial);

        accepted.add(initial);

        context.postBuildHook(() -> resolve_wild_state(context.language, initial));
    }

    private void resolve_wild_state(NLanguage language, NStateSet roots) {
        var queue = new LinkedList<NState>();
        var control = new HashSet<NState>();

        for (var root : roots) {
            for (var trn : root.getTransitions()) {
                queue.add(trn.target);
            }
        }

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var transitions = state.getTransitions();
                var hasWilds = transitions.stream().anyMatch(t -> t.symbol instanceof SymbolWild);
                if (!hasWilds && transitions.size() > 0) {
                    language.transitionWild(state, roots);

                    for (var trn : transitions) {
                        queue.add(trn.target);
                    }
                }
            }
        }
    }

}
